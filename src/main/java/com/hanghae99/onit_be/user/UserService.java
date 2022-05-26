package com.hanghae99.onit_be.user;

import com.hanghae99.onit_be.entity.Plan;
import com.hanghae99.onit_be.entity.User;
import com.hanghae99.onit_be.entity.UserRoleEnum;
import com.hanghae99.onit_be.mypage.ParticipantRepository;
import com.hanghae99.onit_be.noti.NotificationRepository;
import com.hanghae99.onit_be.plan.PlanRepository;

import com.hanghae99.onit_be.user.dto.IdCheckResDto;
import com.hanghae99.onit_be.user.dto.LoginReqDto;
import com.hanghae99.onit_be.user.dto.SignupReqDto;
import com.hanghae99.onit_be.common.utils.Valid;
import com.hanghae99.onit_be.weather.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final NotificationRepository notificationRepository;
    private final ParticipantRepository participantRepository;
    private final WeatherRepository weatherRepository;

    //회원가입 수정 .
    public void registerUser(SignupReqDto requestDto) {

        Valid.validUser(requestDto);

        if (userRepository.existsByUsername(requestDto.getUsername())){
            throw new IllegalArgumentException("이미 사용중인 아이디 입니다!");
        }

        if (userRepository.existsByNickname(requestDto.getNickname())){
            throw new IllegalArgumentException("이미 사용중인 닉네임 입니다!");
        }

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();

        //사용자 ROLE 을 생성 하는 부분 추가 .
        UserRoleEnum role = UserRoleEnum.USER;
        //사용자 profileImg 기본 이미지 부여
        User user = new User(username, password,nickname,role,"https://onit-bucket.s3.ap-northeast-2.amazonaws.com/profile_default.png");
        //return userRepository.save(user);
        userRepository.save(user);
    }

    //아이디 중복검사
    public IdCheckResDto vaildId(LoginReqDto requestDto) {
        String username = requestDto.getUsername();
        IdCheckResDto idCheckDto = new IdCheckResDto();
        idCheckDto.setResult(userRepository.existsByUsername(username));
        return idCheckDto;
    }

    // 클라이언트로 부터 devicetoken 을 받을시에 user 테이블에 devicetoken 저장, token이 존재하면 알림여부 true , 없다면 false
    @Transactional
    public void updateDeviceToken(String token, Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (token != null) {
            user.setNoticeAllowedTrue();
        }
        else {
            user.setNoticeAllowedFalse();
        }
        user.updateToken(token);
    }

    //회원 탈퇴
    @Transactional
    public void deleteUser(User user) {
        participantRepository.deleteAllByUser(user);
        notificationRepository.deleteAllByUser(user);
        List<Plan> planList = planRepository.findAllByUserOrderByPlanDateDesc(user);
        for (Plan plan : planList) {
            weatherRepository.deleteAllByPlanId(plan.getId());
        }
        planRepository.deleteAllByUser(user);
        userRepository.deleteById(user.getId());
    }
}