(ns simple-clojure-ring.core
  (:require [ring.middleware.cookies :refer (wrap-cookies)]
            [ring.adapter.jetty :refer (run-jetty)]))

(defn app [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn get-cookie [name request]
  (get-in request [:cookies name :value]))

(defn wrap-authentication [handler]
  (fn [request]
    (let [session (get-cookie "session" request)]
      (if (= "super-secret-session-id" session)
          (handler request)
          {:status 403 :body "You're not allowed!"}))))

(def secure-app (wrap-authentication app))

(defn -main [& args]
  (run-jetty (wrap-cookies secure-app)
             {:port 8080}))


