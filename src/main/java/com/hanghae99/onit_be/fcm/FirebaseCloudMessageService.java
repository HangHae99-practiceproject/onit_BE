package com.hanghae99.onit_be.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import com.hanghae99.onit_be.entity.Participant;
import com.hanghae99.onit_be.entity.Plan;
import com.hanghae99.onit_be.entity.User;
import com.hanghae99.onit_be.mypage.ParticipantRepository;
import com.hanghae99.onit_be.plan.PlanRepository;
import com.hanghae99.onit_be.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class FirebaseCloudMessageService {

    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/onit-a1529/messages:send";
    private final ObjectMapper objectMapper;
    private final PlanRepository planRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    // 스레드 풀 설정
//    public static ExecutorService newCachedThreadPool() {
//        return new ThreadPoolExecutor(0, 20,
//                60L, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>());
//    }

    // 단일 디바이스 푸쉬(token)
//    public void sendMessageTo( String topic, String title, String body, String url) throws IOException {
//        log.info("1.토큰, 제목, 본문 확인====== " + topic +" ///// "+ title +" ///// "+ body);
//        String message = makeMessage(topic, title, body, url);
//        log.info("2.메세지 값 확인======= " + message);
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .post(requestBody)
//                .addHeader("Authorization", "Bearer " + getAccessToken())
//                .addHeader("Content-Type", "application/json; UTF-8")
//                .build();
//        log.info("3.헤더 토큰 확인======= " + getAccessToken());
//        Response response = client.newCall(request).execute();
//
//        log.info(Objects.requireNonNull(response.body()).string());
//    }

    // 구독 디바이스 푸쉬(topic)
    private void sendSubscribeTopic(List<String> registrationTokens, String planUrl, String planId, String alarmTime)
            throws FirebaseMessagingException, IOException {
        log.info("9.플랜 아이디 확인==== " + planId);
//        List<String> registrationTokens =
//                Collections.singletonList(user.getToken());
        log.info("10.유저 토큰 목록===== " + registrationTokens);

        String fullPlanUrl = "https://imonit.co.kr/details/" + planUrl;
        log.info("11.플랜 링크 확인==== " + fullPlanUrl);

        // 디바이스 토큰 구독하기
        TopicManagementResponse response = FirebaseMessaging.getInstance()
                .subscribeToTopic(registrationTokens, planId);
        log.info("12.유저 토큰들 구독 확인===== " + registrationTokens);

        System.out.println(response.getSuccessCount() + " // 13.토큰들 구독 성공");

        // 구독한 주제에 메세지 요청
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(new WebpushNotification(
                                "온잇(Onit)",
                                "약속시간 1시간 전입니다. 친구들의 위치를 확인해보세요!"))
                        .setFcmOptions(WebpushFcmOptions.withLink(fullPlanUrl))
                        .build())
                .setTopic(planId)
                .build();

        String response2 = FirebaseMessaging.getInstance().send(message);
        log.info("14.구독 메세지 전송===== " + response2);

//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(String.valueOf(message), MediaType.get("application/json; charset=utf-8"));
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .post(requestBody)
//                .addHeader("Authorization", "Bearer " + getAccessToken())
//                .addHeader("Content-Type", "application/json; UTF-8")
//                .build();
//        log.info("13.헤더 토큰 확인======= " + getAccessToken());
//        Response responseFcm = client.newCall(request).execute();
//
//        log.info("14.fcm 서버 응답===== " + Objects.requireNonNull(responseFcm.body()).string());
    }

    // 메세지 요청 후 구독 해제
    public void unsubscribeToTopic (List<String> registrationTokens, String planId)
            throws FirebaseMessagingException, IOException {
        log.info("토큰 리스트 재확인==== " + registrationTokens);
        TopicManagementResponse response3 = FirebaseMessaging.getInstance().unsubscribeFromTopic(
                registrationTokens, planId);
        System.out.println(response3.getSuccessCount() + " // 15.토큰들 구독 해제");
        log.info("토큰 구독 해제 확인==== " + registrationTokens.size());
    }


    @Transactional
    @Scheduled(cron = "0 0/5 * * * *")
    public void noticeScheduler() throws InterruptedException, FirebaseMessagingException, IOException {
        log.info(new Date() + "1.스케쥴러 실행");

        // 오늘의 날짜 구하기
        LocalDate today = LocalDate.now(); // 2022-05-14
        log.info("2.현재 시간===== " + today);
        LocalDateTime todayTime = today.atStartOfDay(); // 2022-05-14 00:00
        log.info("3.오늘 시작 시간===== " + todayTime);
        LocalDateTime tommorrowTime = todayTime.plusDays(1); // 2022-05-15 00:00
        log.info("4.내일 시작 시간===== " + tommorrowTime);

        List<String> registrationTokens = new ArrayList<>();

        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            if ( !(user.getToken() == null) && !(user.getToken().isEmpty())) {
                registrationTokens.add(user.getToken());
            }
        }
        log.info("모든 토큰 리스트 개수==== " + registrationTokens.size());
        unsubscribeToTopic(registrationTokens, "alarm");
        log.info("기존 토큰의 구독 취소==== " + registrationTokens);


//        ExecutorService executorService = newCachedThreadPool();

        // 현 시각 기준으로 오늘의 plan List를 조회 - isAllowed true & 일정이 오늘인 약속들만
        List<Plan> planList = planRepository.findAllByPlanDateBetween(todayTime, tommorrowTime);
        log.info("5.DB 조회 완료");
        log.info("6.오늘의 일정의 수===== " + planList.size());

        LocalDateTime alarmTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("알림시간 전송용==== " + alarmTime);
        List<String> registrationTokens2 = new ArrayList<>();
        // 조회한 plan List 반복문 실행
        for (Plan plan : planList) {
            // 현재시간 기준 1시간 후 = alarmHours
            LocalDateTime planDate = plan.getPlanDate();
            LocalDateTime alarmHour = LocalDateTime.now().plusHours(1);
            int alarm = compareHour(alarmHour, planDate);

            // 알림 시간 == 약속 시간일 때 사용자들에게 알림 푸쉬
            if (alarm == 0) {
                List<Participant> participantList = participantRepository.findAllByPlan(plan);
                log.info("7-3.참가자 수==== " + participantList.size());
                for (Participant participant : participantList) {
                    log.info("8.참가자====" + participant.getUser().getNickname() + " //// " + participant.getUser().getId());
                    User user = participant.getUser();
//                    sendSubscribeTopic(user);
                    String userToken;
                    if (user.getToken() == null && user.getToken().isEmpty()) {
                        userToken = "토큰 발급이 안된 디바이스";
                    } else {
                        userToken = user.getToken();
                    }
                    registrationTokens2.add(userToken);
                }
                sendSubscribeTopic(registrationTokens2,plan.getUrl(), String.valueOf(plan.getId()), String.valueOf(alarmTime));
                unsubscribeToTopic(registrationTokens2, String.valueOf(plan.getId()));
            }
        }
    }

    // 시간 일치 여부
    public static int compareHour(LocalDateTime date1, LocalDateTime date2) {
        LocalDateTime dayDate1 = date1.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime dayDate2 = date2.truncatedTo(ChronoUnit.MINUTES);
        int compareResult = dayDate1.compareTo(dayDate2);
        return compareResult;
    }

    // fcm 메세지 작성
//    private String makeMessage( String topic, String title, String body, String url) throws JsonProcessingException {
//        FcmMessage fcmMessage = FcmMessage.builder()
//                .message(FcmMessage.Message.builder()
////                        .registration_ids(targetTokens)
////                        .token(targetTokens)
//                        .topic(topic)
////                        .allTolkens(registerationTokens)
//                        .notification(FcmMessage.Notification.builder()
//                                .title(title)
//                                .body(body)
//                                .image(null)
//                                .build()
//                        )
//                        .data(FcmMessage.FcmData.builder()
//                                .url(url)
//                                .build()
//                        )
//                        .build()
//                )
//                .validateOnly(false)
//                .build();
//
//        log.info(objectMapper.writeValueAsString(fcmMessage));
//        return objectMapper.writeValueAsString(fcmMessage);
//    }


}