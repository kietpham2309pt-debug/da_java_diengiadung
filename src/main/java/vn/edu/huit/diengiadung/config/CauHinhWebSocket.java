package vn.edu.huit.diengiadung.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import vn.edu.huit.diengiadung.mang.ChatKhachHandler;
import vn.edu.huit.diengiadung.mang.ChatQuanTriHandler;
import vn.edu.huit.diengiadung.service.ChatService;

@Configuration
@EnableWebSocket
public class CauHinhWebSocket implements WebSocketConfigurer {

    private final ChatKhachHandler chatKhachHandler;
    private final ChatQuanTriHandler chatQuanTriHandler;
    private final ChatService chatService;

    public CauHinhWebSocket(ChatKhachHandler chatKhachHandler, ChatQuanTriHandler chatQuanTriHandler, ChatService chatService) {
        this.chatKhachHandler = chatKhachHandler;
        this.chatQuanTriHandler = chatQuanTriHandler;
        this.chatService = chatService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatKhachHandler, "/ws/chat")
                .addInterceptors(new BatTayChat(chatService, false));
        registry.addHandler(chatQuanTriHandler, "/ws/chat-quan-tri")
                .addInterceptors(new BatTayChat(chatService, true));
    }
}
