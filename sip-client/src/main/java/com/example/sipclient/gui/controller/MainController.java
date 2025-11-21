package com.example.sipclient.gui.controller;

import com.example.sipclient.call.CallManager;
import com.example.sipclient.chat.MessageHandler;
import com.example.sipclient.gui.model.Contact;
import com.example.sipclient.gui.model.Message;
import com.example.sipclient.sip.SipUserAgent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 主界面控制器
 */
public class MainController {

    @FXML private ListView<Contact> contactListView;
    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextArea messageInput;
    @FXML private Button sendButton;
    @FXML private Button callButton;
    @FXML private Label chatTitleLabel;
    @FXML private Label statusLabel;

    private SipUserAgent userAgent;
    private CallManager callManager;
    private Contact currentContact;
    private ObservableList<Contact> contacts;

    @FXML
    public void initialize() {
        // 初始化联系人列表
        contacts = FXCollections.observableArrayList();
        contactListView.setItems(contacts);
        contactListView.setCellFactory(lv -> new ContactCell());
        
        // 添加测试联系人
        contacts.add(new Contact("102", "sip:102@10.29.133.174:5060", "用户 102"));
        contacts.add(new Contact("111", "sip:111@10.29.133.174:5060", "用户 111"));
        contacts.add(new Contact("103", "sip:103@10.29.133.174:5060", "用户 103"));
        
        // 监听联系人选择
        contactListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> selectContact(newVal)
        );
        
        // 禁用聊天控件直到选择联系人
        messageInput.setDisable(true);
        sendButton.setDisable(true);
        callButton.setDisable(true);
        
        // 回车发送
        messageInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER") && !event.isShiftDown()) {
                event.consume();
                handleSendMessage();
            }
        });
        
        statusLabel.setText("就绪");
    }

    public void setUserAgent(SipUserAgent userAgent) {
        this.userAgent = userAgent;
        
        // 初始化 CallManager
        callManager = new CallManager();
        userAgent.setCallManager(callManager);
        
        // 设置消息处理器
        userAgent.setMessageHandler(new MessageHandler() {
            @Override
            public void handleIncomingMessage(String from, String body) {
                Platform.runLater(() -> {
                    // 查找或创建联系人
                    Contact contact = findContactByUri(from);
                    if (contact == null) {
                        contact = new Contact(extractUserId(from), from, "用户 " + extractUserId(from));
                        contacts.add(contact);
                    }
                    
                    // 添加消息到联系人
                    Message msg = new Message(body, false, LocalDateTime.now());
                    contact.getMessages().add(msg);
                    contact.setLastMessage(body);
                    contact.setLastMessageTime(LocalDateTime.now());
                    
                    // 如果是当前聊天对象，显示消息
                    if (contact.equals(currentContact)) {
                        displayMessage(msg);
                    } else {
                        // 增加未读计数
                        contact.incrementUnreadCount();
                    }
                    
                    // 刷新列表
                    contactListView.refresh();
                });
            }
        });
        
        // 设置来电监听器
        callManager.setIncomingCallListener((fromUri, sessionId) -> {
            Platform.runLater(() -> showIncomingCallDialog(fromUri, sessionId));
        });
        
        statusLabel.setText("已连接");
    }

    private void selectContact(Contact contact) {
        if (contact == null) return;
        
        currentContact = contact;
        chatTitleLabel.setText(contact.getDisplayName());
        
        // 启用聊天控件
        messageInput.setDisable(false);
        sendButton.setDisable(false);
        callButton.setDisable(false);
        
        // 清空聊天窗口
        chatBox.getChildren().clear();
        
        // 显示历史消息
        for (Message msg : contact.getMessages()) {
            displayMessage(msg);
        }
        
        // 清除未读计数
        contact.clearUnreadCount();
        contactListView.refresh();
        
        // 聚焦输入框
        messageInput.requestFocus();
    }

    @FXML
    private void handleSendMessage() {
        if (currentContact == null) return;
        
        String text = messageInput.getText().trim();
        if (text.isEmpty()) return;
        
        try {
            userAgent.sendMessage(currentContact.getSipUri(), text);
            
            // 添加到聊天记录
            Message msg = new Message(text, true, LocalDateTime.now());
            currentContact.getMessages().add(msg);
            currentContact.setLastMessage(text);
            currentContact.setLastMessageTime(LocalDateTime.now());
            
            displayMessage(msg);
            messageInput.clear();
            contactListView.refresh();
            
        } catch (Exception e) {
            showAlert("发送失败", "无法发送消息: " + e.getMessage());
        }
    }

    @FXML
    private void handleMakeCall() {
        if (currentContact == null) return;
        
        try {
            userAgent.makeCall(currentContact.getSipUri());
            statusLabel.setText("呼叫中: " + currentContact.getDisplayName());
            
            // 打开通话窗口
            showCallWindow(currentContact);
            
        } catch (Exception e) {
            showAlert("呼叫失败", "无法发起呼叫: " + e.getMessage());
        }
    }

    private void displayMessage(Message msg) {
        HBox messageBox = new HBox(10);
        messageBox.setAlignment(msg.isFromMe() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        Label messageLabel = new Label(msg.getContent());
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        messageLabel.setStyle(msg.isFromMe() 
            ? "-fx-background-color: #0084ff; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 15;"
            : "-fx-background-color: #e4e6eb; -fx-text-fill: black; -fx-padding: 10; -fx-background-radius: 15;");
        
        VBox msgContainer = new VBox(5);
        msgContainer.setAlignment(msg.isFromMe() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        Label timeLabel = new Label(msg.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 11px;");
        
        msgContainer.getChildren().addAll(messageLabel, timeLabel);
        messageBox.getChildren().add(msgContainer);
        
        chatBox.getChildren().add(messageBox);
        
        // 滚动到底部
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    private void showIncomingCallDialog(String fromUri, String sessionId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/incoming_call.fxml"));
            Scene scene = new Scene(loader.load());
            
            IncomingCallController controller = loader.getController();
            controller.setCallInfo(fromUri, sessionId, userAgent);
            
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("来电");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCallWindow(Contact contact) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/call.fxml"));
            Scene scene = new Scene(loader.load());
            
            CallController controller = loader.getController();
            controller.setCallInfo(contact, userAgent, callManager);
            
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("通话中 - " + contact.getDisplayName());
            stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Contact findContactByUri(String uri) {
        return contacts.stream()
            .filter(c -> c.getSipUri().equals(uri) || uri.contains(c.getUserId()))
            .findFirst()
            .orElse(null);
    }

    private String extractUserId(String uri) {
        if (uri.contains("@")) {
            String part = uri.substring(uri.indexOf(":") + 1, uri.indexOf("@"));
            return part;
        }
        return uri;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * 联系人列表单元格
     */
    private static class ContactCell extends ListCell<Contact> {
        @Override
        protected void updateItem(Contact contact, boolean empty) {
            super.updateItem(contact, empty);
            
            if (empty || contact == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox vbox = new VBox(5);
                
                HBox topLine = new HBox();
                Label nameLabel = new Label(contact.getDisplayName());
                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                HBox.setHgrow(nameLabel, Priority.ALWAYS);
                
                Label timeLabel = new Label(contact.getLastMessageTime() != null 
                    ? contact.getLastMessageTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    : "");
                timeLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 11px;");
                
                topLine.getChildren().addAll(nameLabel, timeLabel);
                
                HBox bottomLine = new HBox();
                Label msgLabel = new Label(contact.getLastMessage() != null ? contact.getLastMessage() : "");
                msgLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
                msgLabel.setMaxWidth(200);
                HBox.setHgrow(msgLabel, Priority.ALWAYS);
                
                if (contact.getUnreadCount() > 0) {
                    Label badge = new Label(String.valueOf(contact.getUnreadCount()));
                    badge.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; " +
                                 "-fx-background-radius: 10; -fx-padding: 2 6 2 6; -fx-font-size: 11px;");
                    bottomLine.getChildren().addAll(msgLabel, badge);
                } else {
                    bottomLine.getChildren().add(msgLabel);
                }
                
                vbox.getChildren().addAll(topLine, bottomLine);
                setGraphic(vbox);
            }
        }
    }
}
