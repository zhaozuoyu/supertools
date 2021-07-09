package cn.zzy.grpc.consumer.service.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.util.concurrent.SettableFuture;

import cn.zzy.grpc.consumer.ApplicationRun;
import cn.zzy.grpc.user.*;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * @author zhaozuoyu
 * @date 2021/7/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Test
    public void querySimpleInformationTest() {
        try {
            ManagedChannel channel =
                NettyChannelBuilder.forAddress("127.0.0.1", 9090).negotiationType(NegotiationType.PLAINTEXT).build();
            UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(channel);
            RequestDTO requestDTO = RequestDTO.newBuilder().setUserInputDTO(UserInputDTO.newBuilder().setId(100001)
                .setTransactionId(UUID.randomUUID().toString().replaceAll("-", "")).build()).build();
            ResponseDTO responseDTO = blockingStub.querySimpleInformation(requestDTO);
            UserOutputDTO userOutputDTO = responseDTO.getUserOutputDTO();
            logger.info("id:{},name:{},age:{},gender:{}", userOutputDTO.getId(), userOutputDTO.getName(),
                userOutputDTO.getAge(), userOutputDTO.getGender());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void queryServerStreamInformationTest() {
        try {
            ManagedChannel channel =
                NettyChannelBuilder.forAddress("127.0.0.1", 9090).negotiationType(NegotiationType.PLAINTEXT).build();
            UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(channel);
            RequestDTO requestDTO = RequestDTO.newBuilder().setUserInputDTO(UserInputDTO.newBuilder().setId(100001)
                .setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""))).build();
            Iterator<ResponseStreamDTO> iterator = blockingStub.queryServerStreamInformation(requestDTO);
            while (iterator.hasNext()) {
                logger.info("开始遍历*************");
                ResponseStreamDTO responseStreamDTO = iterator.next();
                List<UserOutputDTO> userOutputDTOS = responseStreamDTO.getUserOutputDTOsList();
                for (UserOutputDTO userOutputDTO : userOutputDTOS) {
                    logger.info("id:{},name:{},age:{},gender:{}", userOutputDTO.getId(), userOutputDTO.getName(),
                        userOutputDTO.getAge(), userOutputDTO.getGender());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void queryClientStreamInformationTest() {
        try {
            ManagedChannel channel =
                NettyChannelBuilder.forAddress("127.0.0.1", 9090).negotiationType(NegotiationType.PLAINTEXT).build();
            UserServiceGrpc.UserServiceStub asyncStub = UserServiceGrpc.newStub(channel);
            final SettableFuture<ResponseDTO> finishFuture = SettableFuture.create();
            StreamObserver<ResponseDTO> responseObserver = new StreamObserver<ResponseDTO>() {
                @Override
                public void onNext(ResponseDTO responseDTO) {
                    UserOutputDTO userOutputDTO = responseDTO.getUserOutputDTO();
                    if (userOutputDTO != null) {
                        logger.info("id:{},name:{},age:{},gender:{}", userOutputDTO.getId(), userOutputDTO.getName(),
                            userOutputDTO.getAge(), userOutputDTO.getGender());
                    }
                    finishFuture.set(responseDTO);
                }

                @Override
                public void onError(Throwable throwable) {
                    finishFuture.setException(throwable);
                }

                @Override
                public void onCompleted() {
                    // finishFuture.set(null);
                    logger.info("**********method queryClientStreamInformation execute finished!**********");
                }
            };
            StreamObserver<RequestStreamDTO> requestObserver = asyncStub.queryClientStreamInformation(responseObserver);
            try {
                for (int i = 1; i <= 10; i++) {
                    UserInputDTO userInputDTO = UserInputDTO.newBuilder().setId(100001 + i)
                        .setTransactionId(UUID.randomUUID().toString().replaceAll("-", "")).build();
                    RequestStreamDTO requestStreamDTO =
                        RequestStreamDTO.newBuilder().addUserInputDTOs(userInputDTO).build();
                    requestObserver.onNext(requestStreamDTO);
                    if (finishFuture.isDone()) {
                        break;
                    }
                }
                requestObserver.onCompleted();
                ResponseDTO responseDTO = finishFuture.get();
                if (responseDTO != null) {
                    UserOutputDTO userOutputDTO = responseDTO.getUserOutputDTO();
                    logger.info("id:{},name:{},age:{},gender:{}", userOutputDTO.getId(), userOutputDTO.getName(),
                        userOutputDTO.getAge(), userOutputDTO.getGender());
                }
            } catch (Exception e) {
                requestObserver.onError(e);
                logger.error(e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void queryServerAndClientStreamInformationTest() {
        try {
            ManagedChannel channel =
                NettyChannelBuilder.forAddress("127.0.0.1", 9090).negotiationType(NegotiationType.PLAINTEXT).build();
            UserServiceGrpc.UserServiceStub asyncStub = UserServiceGrpc.newStub(channel);
            final SettableFuture<List<ResponseStreamDTO>> finishFuture = SettableFuture.create();
            StreamObserver<ResponseStreamDTO> responseObserver = new StreamObserver<ResponseStreamDTO>() {
                List<ResponseStreamDTO> responseStreamDTOS = new ArrayList<>();

                @Override
                public void onNext(ResponseStreamDTO responseStreamDTO) {
                    responseStreamDTOS.add(responseStreamDTO);
                }

                @Override
                public void onError(Throwable throwable) {
                    finishFuture.setException(throwable);
                }

                @Override
                public void onCompleted() {
                    finishFuture.set(responseStreamDTOS);
                    logger.info("**********method queryServerAndClientStreamInformation execute finished!**********");
                }
            };
            StreamObserver<RequestStreamDTO> requestObserver =
                asyncStub.queryServerAndClientStreamInformation(responseObserver);
            try {
                for (int i = 1; i <= 10; i++) {
                    UserInputDTO userInputDTO =
                        UserInputDTO.newBuilder().setId(100001 + i).setTransactionId(String.valueOf(i)).build();
                    RequestStreamDTO requestStreamDTO =
                        RequestStreamDTO.newBuilder().addUserInputDTOs(userInputDTO).build();
                    requestObserver.onNext(requestStreamDTO);
                    if (finishFuture.isDone()) {
                        break;
                    }
                }
                requestObserver.onCompleted();
                List<ResponseStreamDTO> responseStreamDTOS = finishFuture.get();
                for (ResponseStreamDTO responseStreamDTO : responseStreamDTOS) {
                    if (responseStreamDTO != null) {
                        List<UserOutputDTO> userOutputDTOS = responseStreamDTO.getUserOutputDTOsList();
                        if (userOutputDTOS != null && userOutputDTOS.size() > 0) {
                            for (UserOutputDTO userOutputDTO : userOutputDTOS) {
                                logger.info("id:{},name:{},age:{},gender:{}", userOutputDTO.getId(),
                                    userOutputDTO.getName(), userOutputDTO.getAge(), userOutputDTO.getGender());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                requestObserver.onError(e);
                logger.error(e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
