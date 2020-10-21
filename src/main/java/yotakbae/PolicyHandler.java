package yotakbae;

import yotakbae.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    PaymentAggRepository paymentAggRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReqCanceled_PaymentCancelPol(@Payload ReqCanceled reqCanceled){

        if(reqCanceled.isMe()){
            //LJK

            Iterator<PaymentAgg> iterator = paymentAggRepository.findAll().iterator();
            while(iterator.hasNext()){
                PaymentAgg paymenttmp = iterator.next();
                if(paymenttmp.getRequestId() == reqCanceled.getId()){
                    Optional<PaymentAgg> PaymentOptional = paymentAggRepository.findById(paymenttmp.getId());
                    PaymentAgg payment = PaymentOptional.get();
                    payment.setStatus(reqCanceled.getStatus());
                    paymentAggRepository.save(payment);
                }
            }

            //LJK
            //System.out.println("##### listener PaymentCancelPol : " + reqCanceled.toJson());

                    }
    }

}
