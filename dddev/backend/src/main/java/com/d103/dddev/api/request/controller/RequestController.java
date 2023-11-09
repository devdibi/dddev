package com.d103.dddev.api.request.controller;

import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.request.collection.Comment;
import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestResponseDto;
import com.d103.dddev.api.request.service.RequestServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ground/{groundId}/request")
@RequiredArgsConstructor
@Api(tags = {"요청 문서 API"})
public class RequestController {
    private final RequestServiceImpl requestService;

    @PostMapping("/create")
    @ApiOperation(value="요청 문서 생성")
    public ResponseEntity<?> insertRequest(@PathVariable("groundId") int groundId,
                                           @ApiParam(value = "step1문서 생성할 때 parentId 필요없음\n" +
                                                   "미분류로 생성할 때 parentId 미분류 문서 id\n" +
                                                   "title -> not required 없으면 빈 문자열 \"\"로 생성")@RequestBody RequestInsertOneDto requestInsertOneDto,
                                           @RequestHeader String Authorization,
                                           @AuthenticationPrincipal UserDetails userDetails){
        ResponseDto<Request> responseDto;

        try{
            Request request = requestService.insertRequest(groundId, requestInsertOneDto, userDetails);
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서가 생성되었습니다.")
                    .data(request)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/titles")
    @ApiOperation(value="제목들로 step1 요청 문서들 생성")
    public ResponseEntity<?> insertRequestsWithTitles(@PathVariable("groundId") int groundId,
                                                      @RequestBody RequestInsertManyDto requestInsertManyDto,
                                                      @RequestHeader String Authorization,
                                                      @AuthenticationPrincipal UserDetails userDetails){
        ResponseDto<List<Request>> responseDto;

        try{
            List<Request> requestList = requestService.insertRequestsWithTitles(groundId, requestInsertManyDto, userDetails);
            responseDto = ResponseDto.<List<Request>>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서들이 생성되었습니다.")
                    .data(requestList)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<Request>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{requestId}")
    @ApiOperation(value="요청 문서 상세 조회")
    public ResponseEntity<?> getRequest(@PathVariable("groundId") int groundId, @PathVariable("requestId") String RequestId,
                                        @RequestHeader String Authorization){
        ResponseDto<Request> responseDto;

        try{
            Request Request = requestService.getRequest(groundId, RequestId);
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서를 불러왔습니다.")
                    .data(Request)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total")
    @ApiOperation(value="step1 문서들 불러오기")
    public ResponseEntity<?> getStep1Requests(@PathVariable("groundId") int groundId,
    @RequestHeader String Authorization){
        ResponseDto<List<RequestResponseDto>> responseDto;

        try{
            List<RequestResponseDto> requests = requestService.getStep1Requests(groundId);
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서들을 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step2")
    @ApiOperation(value="step2 문서들 불러오기")
    public ResponseEntity<?> getStep2Requests(@PathVariable("groundId") int groundId,
                                              @RequestHeader String Authorization){
        ResponseDto<List<Request>> responseDto;

        try{
            List<Request> Requests = requestService.getStep2Requests(groundId);
            responseDto = ResponseDto.<List<Request>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step2 문서들을 불러왔습니다.")
                    .data(Requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<Request>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{requestId}")
    @ApiOperation(value="요청 문서 수정")
    public ResponseEntity<?> updateRequest(@PathVariable("groundId") int groundId,
                                           @PathVariable("requestId") String requestId,
                                           @RequestBody RequestUpdateDto requestUpdateDto,
                                           @RequestHeader String Authorization,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDto<Request> responseDto;

        try{
            Request Request = requestService.updateRequest(groundId, requestId, requestUpdateDto, userDetails);
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 수정했습니다.")
                    .data(Request)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/send/{requestId}")
    @ApiOperation(value="요청 문서 요청보내기")
    public ResponseEntity<?> sendRequest(@PathVariable("groundId") int groundId,
                                         @PathVariable("requestId") String requestId,
                                         @RequestBody RequestUpdateDto requestUpdateDto,
                                         @RequestHeader String Authorization,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDto<Request> responseDto;

        try{
            requestService.sendRequest(groundId, requestId, requestUpdateDto, userDetails);
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청을 보냈습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{requestId}/createComment")
    @ApiOperation(value="요청 문서 댓글달기")
    public ResponseEntity<?> createComment(@PathVariable("groundId") int groundId,
                                         @PathVariable("requestId") String requestId,
                                         @RequestBody String comment,
                                         @RequestHeader String Authorization,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDto<Comment> responseDto;

        try{
            Comment saveComment = requestService.createComment(groundId, requestId, comment, userDetails);
            responseDto = ResponseDto.<Comment>builder()
                    .code(HttpStatus.OK.value())
                    .message("댓글을 달았습니다.")
                    .data(saveComment)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Comment>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/move/{requestId}")
    @ApiOperation(value="요청 문서 위치이동")
    public ResponseEntity<?> moveRequest(@PathVariable("groundId") int groundId,
                                         @PathVariable("requestId") String requestId,
                                         @ApiParam(value= "parentId -> 목적지 부모의 아이디") @RequestBody RequestMoveDto requestMoveDto,
                                         @RequestHeader String Authorization) {
        ResponseDto<Request> responseDto;

        try{
            Request Request = requestService.moveRequest(groundId, requestId, requestMoveDto);
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 이동했습니다.")
                    .data(Request)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{requestId}")
    @ApiOperation(value="요청 문서 삭제")
    public ResponseEntity<?> deleteRequest(@PathVariable("groundId") int groundId, @PathVariable("requestId") String requestId,@RequestHeader String Authorization){

        ResponseDto<Request> responseDto;

        try{
            requestService.deleteRequest(groundId, requestId);
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{requestId}/title")
    @ApiOperation(value="요청 문서 제목 수정")
    public ResponseEntity<?> updateRequest(@PathVariable("groundId") int groundId,
                                           @PathVariable("requestId") String requestId,
                                           @RequestBody RequestTitleDto requestTitleDto,
                                           @RequestHeader String Authorization,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDto<Request> responseDto;

        try{
            Request Request = requestService.titleRequest(groundId, requestId, requestTitleDto, userDetails);
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서의 제목을 수정했습니다.")
                    .data(Request)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
