# CraftDemo

Test Cases: 

1. *Test without API_KEY*
    * Verify the HTTP Response Status Code as 403 (Forbidden).
    * Verify that Response has Error Code as "API_KEY_MISSING".
    * No api_key was supplied. Get one at https://api.nasa.gov.

2. *Test with Invalid API_KEY*
    * Verify the HTTP Response Status Code as 403 (Forbidden).
    * Verify that Response has Error Code as "API_KEY_INVALID".
    * Verify that Response has Error Message as "An invalid api_key was supplied. Get one at https://api.nasa.gov".
     
3. *Test with Valid API_KEY with Query Parameter q=apollo*
    * Verify that HTTP Response Status Code as 200 (Forbidden).
    * Verify that Response is against JSON Schema. 
    * Verify that Response doesn't have any Error message
    * Verify that Response Header has X-RateLimit-Limit:1000 and X-RateLimit-Remaining:997
    * Verify that Response Header has decrement by 1
    
