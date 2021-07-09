package cn.zzy.grpc.producer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzy.grpc.user.*;
import io.grpc.stub.StreamObserver;

/**
 * @author zhaozuoyu
 * @date 2021/7/6
 */
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void querySimpleInformation(RequestDTO request, StreamObserver<ResponseDTO> responseObserver) {
        UserInputDTO userInputDTO = request.getUserInputDTO();
        int id = userInputDTO.getId();
        String transactionId = userInputDTO.getTransactionId();
        logger.info("properties id:{},transactionId:{}", id, transactionId);
        try {
            UserOutputDTO userOutputDTO =
                UserOutputDTO.newBuilder().setId(id).setName("Elise").setAge(18).setGender("女").build();
            ResponseDTO responseDTO = ResponseDTO.newBuilder().setUserOutputDTO(userOutputDTO).build();
            logger.info("method querySimpleInformation result:{}", responseDTO.toString());
            responseObserver.onNext(responseDTO);
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void queryServerStreamInformation(RequestDTO request, StreamObserver<ResponseStreamDTO> responseObserver) {
        UserInputDTO userInputDTO = request.getUserInputDTO();
        int id = userInputDTO.getId();
        String transactionId = userInputDTO.getTransactionId();
        logger.info("properties id:{},transactionId:{}", id, transactionId);
        try {
            ResponseStreamDTO.Builder builder = ResponseStreamDTO.newBuilder();
            for (int i = 1; i <= 10; i++) {
                UserOutputDTO userOutputDTO =
                    UserOutputDTO.newBuilder().setId(id).setName("Elise" + i).setAge(18).setGender("女").build();
                builder.addUserOutputDTOs(userOutputDTO);
            }
            ResponseStreamDTO responseStreamDTO = builder.build();
            logger.info("method querySimpleInformation result:{}", responseStreamDTO.toString());
            responseObserver.onNext(responseStreamDTO);
            // queryServerStreamInformation方法为服务端流式RPC，故可以继续调用
            responseObserver.onNext(responseStreamDTO);
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public StreamObserver<RequestStreamDTO> queryClientStreamInformation(StreamObserver<ResponseDTO> responseObserver) {
        return new StreamObserver<RequestStreamDTO>() {
            final List<RequestStreamDTO> requestStreamDTOS = new ArrayList<>();

            @Override
            public void onNext(RequestStreamDTO requestStreamDTO) {
                requestStreamDTOS.add(requestStreamDTO);
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            }

            /**
             * 在客户端结束写入消息时调用
             */
            @Override
            public void onCompleted() {
                // TODO 可以根据入参做相应的业务逻辑处理，这里仅做打印
                for (RequestStreamDTO requestStreamDTO : requestStreamDTOS) {
                    List<UserInputDTO> userInputDTOS = requestStreamDTO.getUserInputDTOsList();
                    for (UserInputDTO userInputDTO : userInputDTOS) {
                        // 打印入参
                        logger.info("properties id:{},transactionId:{}", userInputDTO.getId(),
                            userInputDTO.getTransactionId());
                    }
                }
                // 返回处理后的结果
                UserOutputDTO userOutputDTO =
                    UserOutputDTO.newBuilder().setId(100002).setName("Jason").setAge(22).setGender("男").build();
                ResponseDTO responseDTO = ResponseDTO.newBuilder().setUserOutputDTO(userOutputDTO).build();
                logger.info("method queryClientStreamInformation result:{}", responseDTO.toString());
                try {
                    responseObserver.onNext(responseDTO);
                    responseObserver.onCompleted();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        };
    }

    @Override
    public StreamObserver<RequestStreamDTO>
        queryServerAndClientStreamInformation(StreamObserver<ResponseStreamDTO> responseObserver) {
        return new StreamObserver<RequestStreamDTO>() {

            @Override
            public void onNext(RequestStreamDTO requestStreamDTO) {
                List<UserInputDTO> userInputDTOS = requestStreamDTO.getUserInputDTOsList();
                if (userInputDTOS != null && userInputDTOS.size() > 0) {
                    for (UserInputDTO userInputDTO : userInputDTOS) {
                        UserOutputDTO userOutputDTO = UserOutputDTO.newBuilder().setId(userInputDTO.getId())
                            .setName("Mike").setAge(26).setGender("男").build();
                        ResponseStreamDTO responseStreamDTO =
                            ResponseStreamDTO.newBuilder().addUserOutputDTOs(userOutputDTO).build();
                        logger.info("method queryServerAndClientStreamInformation result:{}",
                            responseStreamDTO.toString());
                        try {
                            responseObserver.onNext(responseStreamDTO);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
