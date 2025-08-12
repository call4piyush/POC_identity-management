/*
 * Copyright 2025 Piyush Joshi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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


