package com.project.mKajy.gateway.subscription;

import com.project.mKajy.model.OmKycSubscriptionRequest;

import gateway.dto.CbsSubscriptionResult;

public interface ICbsSubscriptionGateway {
    CbsSubscriptionResult sendSubscription(OmKycSubscriptionRequest kyc);
}