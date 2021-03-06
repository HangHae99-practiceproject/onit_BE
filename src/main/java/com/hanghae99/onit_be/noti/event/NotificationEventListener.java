package com.hanghae99.onit_be.noti.event;

import com.hanghae99.onit_be.entity.Plan;
import com.hanghae99.onit_be.entity.User;
import com.hanghae99.onit_be.noti.*;
import com.hanghae99.onit_be.mypage.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.hanghae99.onit_be.noti.NotificationController.sseEmitters;


@Async
@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private Set<SseEmitter> emitterSet = new CopyOnWriteArraySet<>();
    private final ParticipantRepository participantRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final EmitterRepository emitterRepository;


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleParticipateEvent(ParticipateEvent event) {
        log.info(event.toString());
        log.info("111111111111111번");
        Long userId = event.getUser().getId();

        Notification notification1 = createNotification(event.getPlan(), event.getUser(), event.getMessage(), NotificationType.PARTICIPATE);
        sendEvent(userId, notification1);

    }


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePlanUpdateEvent(@NotNull PlanUpdateEvent planUpdateEvent) {
        Plan plan = participantRepository.findByUserAndPlan(planUpdateEvent.getUser(), planUpdateEvent.getPlan()).getPlan();
        log.info("plan======================", planUpdateEvent.getPlan().getPlanName());
        Long userId = planUpdateEvent.getUser().getId();

        Notification notification1 = createNotification(plan, planUpdateEvent.getUser(), planUpdateEvent.getMessage(), NotificationType.UPDATE);
        sendEvent(userId, notification1);

    }


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePlanDeleteEvent(@NotNull PlanDeleteEvent planDeleteEvent) {
        Plan plan = participantRepository.findByUserAndPlan(planDeleteEvent.getUser(), planDeleteEvent.getPlan()).getPlan();
        Long userId = planDeleteEvent.getUser().getId();
        Notification notification1 = createNotification(plan, planDeleteEvent.getUser(), planDeleteEvent.getMessage(), NotificationType.DELETE);
        sendEvent(userId, notification1);

    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleLeaveEvent(@NotNull LeaveEvent leaveEvent) {
        Plan plan = participantRepository.findByUserAndPlan(leaveEvent.getUser(), leaveEvent.getPlan()).getPlan();
        Long userId = leaveEvent.getUser().getId();

        Notification notification1 = createNotification(plan, leaveEvent.getUser(), leaveEvent.getMessage(), NotificationType.LEAVE);
        sendEvent(userId, notification1);
    }

    private Notification updateNotification(Plan plan, User user, String message, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.update(plan, user, message, notificationType);
        return notification;
    }

    private void sendEvent(Long userId, Notification notification1) {
        log.info("send = 1번 ", notification1.getNotificationType());
        if (sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitters.get(userId);
            log.info("send = 2번", sseEmitter);
            try {
                sseEmitter.send(SseEmitter.event().name("addNotice").data(notification1));
                log.info("send = 3번", sseEmitter);
            } catch (Exception e) {
                sseEmitters.remove(userId);
            }
        }
    }


    private Notification createNotification(Plan plan, User user, String message, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setPlan(plan);
        notification.setUser(user);
        notification.setNotificationType(notificationType);
        notification.setMessage(message);
        notification.setRead(false);
        notificationRepository.save(notification);
        return notification;
    }

//    public void send(Optional<Notification> notification) {
//
//        String id = "";
//        if (notification.isPresent()) {
//            String id = String.valueOf(notification.);
//        }
//        // 로그인 한 유저의 SseEmitter 모두 가져오기
//        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(id);
//        sseEmitters.forEach(
//                (key, emitter) -> {
//                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
//                    emitterRepository.saveEventCache(key, notification);
//                    // 데이터 전송
//                    notificationService.sendToClient(emitter, key, NotificationResponse.from(notification));
//                }
//        );
//    }

//    public void send2(Notification notification) {
//
//        String id = String.valueOf(notification.getId());
//
//        // 로그인 한 유저의 SseEmitter 모두 가져오기
//        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(id);
//        sseEmitters.forEach(
//                (key, emitter) -> {
//                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
//                    emitterRepository.saveEventCache(key, notification);
//                    // 데이터 전송
//                    notificationService.sendToClient(emitter, key,notification);
//                }
//        );
//    }

    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
    }

}
