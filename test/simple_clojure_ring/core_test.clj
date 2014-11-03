(ns simple-clojure-ring.core-test
  (:require [clojure.test :refer :all]
            [simple-clojure-ring.core :refer (secure-app)]))

(defn set-cookie [name value request]
  (assoc-in request [:cookies name :value] value))

(def request { })
(def authed-request 
  (set-cookie "session" "super-secret-session-id" request))

(deftest security-policy
  (let [good-response (secure-app authed-request)
        ; remember that ring apps are just functions!
        bad-response (secure-app request)]
    (is (= 200 (:status good-response)))
    (is (= "Hello World" (:body good-response)))
    (is (= 403 (:status bad-response)))
    (is (= "You're not allowed!" (:body bad-response)))))
