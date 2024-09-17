package org.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpServiceGrpcImpl extends SignUpServiceGrpc.SignUpServiceImplBase {

    @Autowired
    private SignUpService signUpService;

    @Override
    public void signUp(SignUpRequest request, StreamObserver<Empty> responseObserver) {
        System.out.println("Request received from client:\n" + request);


        try {
            signUpService.signUp(request);
            System.out.println("User saved successfully.");
        } catch (Exception e) {
            if(e.getMessage().contains("[SQLITE_CONSTRAINT_UNIQUE]")) {
                responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
            } else{
                e.printStackTrace();
                responseObserver.onError(e);
            }
            return;
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}

