package app.rbzeta.edcimplementationtracker.network;

import app.rbzeta.edcimplementationtracker.application.AppConfig;
import app.rbzeta.edcimplementationtracker.model.EDCItem;
import app.rbzeta.edcimplementationtracker.model.User;
import app.rbzeta.edcimplementationtracker.network.response.EDCResponseMessage;
import app.rbzeta.edcimplementationtracker.network.response.LoginResponseMessage;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Robyn on 12/30/2016.
 */

public interface NetworkApi {
    @GET(AppConfig.GET_EDC_LIST_STAGING_URL)
    Observable<EDCResponseMessage> getEDCListStagingFromServer(@Query("page") int page,
                                                               @Query("branch") int branch,
                                                               @Query("type") String type);

    @Multipart
    @POST(AppConfig.SUBMIT_EDC_IMPLEMENTATION_URL)
    Observable<EDCResponseMessage> submitEDCImplementation(@Part("edc") EDCItem edc,
                                                      @Part MultipartBody.Part photo,
                                                      @Part MultipartBody.Part photoBA);

    @GET(AppConfig.GET_EDC_LIST_COMPLETED_URL)
    Observable<EDCResponseMessage> getEDCListImplementedFromServer(@Query("page") int page,
                                                               @Query("branch") int branch,
                                                               @Query("type") String type);

    @GET(AppConfig.GET_EDC_LIST_VERIFIED_URL)
    Observable<EDCResponseMessage> getEDCListVerifiedFromServer(@Query("page") int page,
                                                                   @Query("branch") int branch,
                                                                   @Query("type") String type);

    @GET(AppConfig.USER_LOGIN_URL)
    Observable<LoginResponseMessage> userLogin(@Query("pn") String pn, @Query("password")String password);

    @POST(AppConfig.VERIFIED_EDC_IMPLEMENTATION_URL)
    Observable<EDCResponseMessage> verifyEDCImplementation(@Body EDCItem edc);

    @POST(AppConfig.CANCELED_EDC_IMPLEMENTATION_URL)
    Observable<EDCResponseMessage> cancelEDCImplementation(@Body EDCItem edc);

    @POST(AppConfig.CANCELED_EDC_VERIFICATION_URL)
    Observable<EDCResponseMessage> cancelEDCVerification(@Body EDCItem item);
}
