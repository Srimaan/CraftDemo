# CraftDemo

<h1> Test Cases: </h1>

<h4> 1. Test without API_KEY </h4>
    * Verify the HTTP Response Status Code as 403 (Forbidden).
    * Verify that Response has Error Code as "API_KEY_MISSING".
    * No api_key was supplied. Get one at https://api.nasa.gov.

<h4> 2. Test with Invalid API_KEY </h4>
    * Verify the HTTP Response Status Code as 403 (Forbidden).
    * Verify that Response has Error Code as "API_KEY_INVALID".
    * Verify that Response has Error Message as "An invalid api_key was supplied. Get one at https://api.nasa.gov".
     
<h4> 3. Test with Valid API_KEY with Query Parameter q=apollo </h4>
    * Verify that HTTP Response Status Code as 200.
    * Verify that Response is against JSON Schema. 
    * Verify that Response doesn't have any Error message
    * Verify that Response Header has X-RateLimit-Limit:1000 and X-RateLimit-Remaining:997
    * Verify that Response Header has X-RateLimit-Remaining decrement by 1
    
<h4> 4. Test with OVER_RATE_LIMIT (using DEMO_KEY) </h4>
    * Verify that HTTP Response Status Code as 429 (too many Parameters)
    * Assuming that we have already sent 40 Requests from the Same IP Address (DEMO_KEY Limit is 40).
    * Verify that Response Header has X-RateLimit-Limit:40 and X-RateLimit-Remaining:0
     
<h4> 5. Test with Valid API_KEY without Query Parameter </h4>
      example : https://api.nasa.gov/planetary/sounds?api_key=79U7LZO3OguHmTUfdN4UhOUVZOgMzzZwITEST
    * Verify that HTTP Response Status Code as 200.
    * Verify that Response is against JSON Schema.
    * Verify that Response doesn't have any Error message
    * Verify that Response Header has X-RateLimit-Limit:1000 and X-RateLimit-Remaining:997
    * Verify that Response Header has X-RateLimit-Remaining decrement by 1  
    
