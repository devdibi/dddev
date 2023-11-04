package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.IssueMessage;
import com.d103.dddev.api.issue.repository.IssueRepository;
import com.d103.dddev.api.issue.util.IssueUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@AllArgsConstructor
public class IssueServiceImpl implements IssueService{

    private final IssueRepository issueRepository;
    private final IssueUtil issueUtil;

    /*
     * groundId는 필수 값
     * 스프린트 내에서 생성하는 경우에는 sprintId 같이 저장
     * 상위 문서에서 생성하는 경우 parentId 같이 저장
     * 상위 문서 내에서 생성하는 경우 상위 문서의 step + 1
     * 별도로 step을 지정하는 경우는 그 값을 저장
     *
     *
     */

    // 문서의 최초 생성 시 기본 구조대로 생성한다.
    // 그라운드ID는 필수로 받아야한다.


    // 공통 조건
    /*
     * 1. 생성자, 수정자, 생성 시간, 수정 시간은 무조건 자동 생성
     * 2. 연결 수행 시 무조건 상위 문서에 하위 문서의 ID를 추가
     *  */

    @Override
    public IssueDto.Create.Response issueCreate(String groundId, IssueDto.Create.Request request) {

        Issue issue = Issue.builder()
                .groundId(groundId)
                .parentId(issueUtil.unclassified(request.getParentId(),groundId,"check"))
                .sprintId(request.getSprintId() != null && !request.getSprintId().isEmpty() ? request.getSprintId() : "")
                .step(3) // 고정값
                .workTime(0) // 기본값
                .studyTime(0) // 기본값
                .type("issue")
                .status(0) // 0 : 미분류, 1 : 진행 예정, 2 : 진행 중, 3 : 완료
                .title("")
                .content("")
                .build();

        issueRepository.save(issue); // 이슈 저장

        Issue check = issueRepository.findById(issueUtil.unclassified(request.getParentId(),groundId,"check"))
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 연결할 문서 조회

        List<String> children = check.getChildrenId(); // 하위 문서 조회

        children.add(issue.getId()); // 하위 문서 ID 추가

        check.setChildrenId(children); // 객체에 값 변경

        issueRepository.save(check); // DB 저장

        return IssueDto.Create.Response.builder()
                .message(IssueMessage.create())
                .status(HttpStatus.OK.value())
                .issue(issue)
                .build();
    }

    @Override
    public IssueDto.List.Response issueList(String groundId, String checkId) {
        ArrayList<Issue> issueList = issueRepository.findAllByGroundIdAndParentIdAndType(groundId, checkId,"issue");

        if(issueList.isEmpty()){
            return IssueDto.List.Response.builder()
                    .message(IssueMessage.emptyList())
                    .status(HttpStatus.OK.value())
                    .issueList(issueList)
                    .build();
        }

        return IssueDto.List.Response.builder()
                .message(IssueMessage.list())
                .status(HttpStatus.OK.value())
                .issueList(issueList)
                .build();
    }

    @Override
    public IssueDto.Detail.Response issueDetail(String groundId, String issueId) {
        Issue issue = issueRepository.findByGroundIdAndId(groundId, issueId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        if(issue.getStep() != 3){
            throw new NoSuchElementException(Error.WrongStep());
        }

        return IssueDto.Detail.Response.builder()
                .message(IssueMessage.detail())
                .status(HttpStatus.OK.value())
                .issue(issue)
                .build();
    }

    @Override
    public IssueDto.Delete.Response issueDelete(String issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 이슈 정보 조회

        Issue check = issueRepository.findById(issue.getParentId())
                        .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 상위 체크 문서 조회

        List<String> children = check.getChildrenId(); // 하위 문서 목록

        children.remove(issue.getId()); // 삭제할 이슈 삭제

        check.setChildrenId(children); // 체크 포인트 문서 객체 수정

        issueRepository.deleteById(issueId); // 이슈 문서 삭제

        issueRepository.save(check); // 상위 체크 포인트 문서 최신화

        return IssueDto.Delete.Response.builder()
                .message(IssueMessage.delete())
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public IssueDto.Content.Response issueContent(IssueDto.Content.Request request, String issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        issue.setTitle(request.getTitle());
        issue.setContent(request.getContent());

        issueRepository.save(issue);

        return IssueDto.Content.Response.builder()
                .message(IssueMessage.content())
                .status(HttpStatus.OK.value())
                .issue(issue)
                .build();
    }

    @Override
    public IssueDto.Status.Response issueStatus(IssueDto.Status.Request request, String issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        issue.setStatus(request.getStatus());

        issueRepository.save(issue);

        return IssueDto.Status.Response.builder()
                .message(IssueMessage.status())
                .status(HttpStatus.OK.value())
                .issue(issue)
                .build();
    }

    @Override
    public IssueDto.Connect.Response issueConnect(IssueDto.Connect.Request request, String issueId) {
        Issue issue = issueRepository.findById(issueId)
                        .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 이슈 조회

        Issue oldCheck = issueRepository.findById(issue.getParentId())
                .orElseThrow(() -> new NoSuchElementException(Error.NoParentDocument())); // 연결 된 체크 포인트 문서 조회

        Issue newCheck = issueRepository.findById(request.getParentId())
                .orElseThrow(() -> new NoSuchElementException(Error.NoParentDocument())); // 연결할 체크 포인트 문서 조회

        List<String> oldChildren = oldCheck.getChildrenId(); // 기존 연결 된 문서
        List<String> newChildren = newCheck.getChildrenId(); // 연결 할 문서의 하단

        // 최신화
        oldChildren.remove(issue.getId());
        newChildren.add(issue.getId());
        issue.setParentId(newCheck.getId());

        oldCheck.setChildrenId(oldChildren);
        newCheck.setChildrenId(newChildren);

        // 저장
        issueRepository.save(oldCheck);
        issueRepository.save(newCheck);
        issueRepository.save(issue);

        return IssueDto.Connect.Response.builder()
                .message(IssueMessage.connect())
                .status(HttpStatus.OK.value())
                .issue(issue)
                .build();
    }

    @Override
    public IssueDto.Time.Response issueTime(IssueDto.Time.Request request, String issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        issue.setWorkTime(request.getWorkTime());
        issue.setStudyTime(request.getStudyTime());

        issueRepository.save(issue);

        return IssueDto.Time.Response.builder()
                .message(IssueMessage.time())
                .status(HttpStatus.OK.value())
                .issue(issue)
                .build();
    }

    @Override
    public IssueDto.Sprint.Response issueSprint(IssueDto.Sprint.Request request, String issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        issue.setSprintId(request.getSprintId());

        issueRepository.save(issue);

        return IssueDto.Sprint.Response.builder()
                .message(IssueMessage.sprint())
                .status(HttpStatus.OK.value())
                .issue(issue)
                .build();
    }



}