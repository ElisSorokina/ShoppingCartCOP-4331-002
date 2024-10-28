package org.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerServiceGrpcImpl extends BuyerServiceGrpc.BuyerServiceImplBase {
    @Autowired
    private BuyerService buyerService;

    @Override
    public void getItemList(GetItemListRequest request, StreamObserver<GetItemListResponse> responseObserver) {
        super.getItemList(request, responseObserver);
    }

    @Override
    public void addItemToCart(AddItemRequest request, StreamObserver<Empty> responseObserver) {
        super.addItemToCart(request, responseObserver);
    }

    @Override
    public void updateCart(UpdateCartRequest request, StreamObserver<Empty> responseObserver) {
        super.updateCart(request, responseObserver);
    }

    @Override
    public void deleteItemFromCart(DeleteItemRequest request, StreamObserver<Empty> responseObserver) {
        super.deleteItemFromCart(request, responseObserver);
    }
}
