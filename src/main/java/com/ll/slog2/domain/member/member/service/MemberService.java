package com.ll.slog2.domain.member.member.service;

import com.ll.slog2.domain.auth.auth.service.AuthTokenService;
import com.ll.slog2.domain.member.member.entity.Member;
import com.ll.slog2.domain.member.member.repository.MemberRepository;
import com.ll.slog2.global.exceptions.GlobalException;
import com.ll.slog2.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public RsData<Member> join(String username, String password, String nickname) {
        return join(username, password, nickname, null);
    }

    @Transactional
    public RsData<Member> join(String username, String password, String nickname, String profileImgUrl) {
        findByUsername(username).ifPresent(ignored -> {
            throw new GlobalException("400-1", "%s(은)는 이미 존재하는 아이디입니다.".formatted(username));
        });

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .refreshToken(authTokenService.genRefreshToken())
                .build();

        memberRepository.save(member);

        return RsData.of("회원가입이 완료되었습니다.", member);
    }

    public long count() {
        return memberRepository.count();
    }

    public boolean matchPassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public Member modifyOrJoin(String username, String providerTypeCode, String nickname, String profileImgUrl) {
        Member member = memberRepository.findByUsername(username).orElseGet(() -> {
            return join(username, "", nickname, profileImgUrl).getData();
        });

        member.setNickname(nickname);
        member.setProfileImgUrl(profileImgUrl);
        return member;
    }
}