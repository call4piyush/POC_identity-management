org.springframework.cloud.contract.spec.Contract.make {
    description "Should return public success response"
    request {
        method 'GET'
        url '/api/public/success'
    }
    response {
        status 200
        headers { contentType(applicationJson()) }
        body(
                status: 'success',
                message: 'Public success endpoint is reachable'
        )
    }
}


