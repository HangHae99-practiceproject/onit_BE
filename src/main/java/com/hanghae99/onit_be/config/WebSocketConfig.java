package com.hanghae99.onit_be.config;

import com.hanghae99.onit_be.websocket.ChannelInterceptorImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChannelInterceptorImpl channelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // wss://imonint.shop/ws
        registry.addEndpoint("/ws")
                .setAllowedOrigins("https://localhost:3000","https://imonit.co.kr")
//              ("https://localhost:3000", "https://localhost:8080", "http://localhost:8080/", "http://localhost:3000", "https://imonint.shop/ws/**", "https://imonint.shop/ws", "https://onit-a1529.firebaseapp.com/")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // prefix - queue : 1 대 1 통신 , topic : 1 대 N 통신
        registry.enableSimpleBroker("/topic");
        // 데이터 가공 후 메세지 브로커에게 전달해야 할 경우 핸들러를 거쳐야 한다
        // 해당 url 로 접근하면 해당 경로를 처리하고 있는 핸들러로 접근
        registry.setApplicationDestinationPrefixes("/maps");
    }

    @Override
    // 유저의 CONNECT와 DISCONNECT 할때의 정보를 가져오기
    // StompHandler가 websocket 앞단에서 token 체크
    public void configureClientInboundChannel(@NotNull ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }
}