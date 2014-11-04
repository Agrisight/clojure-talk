(ns simple-clojure-ring.extras.compojure
  (:require [compojure.core :refer (defroutes GET POST)]
            [ring.middleware.params :refer (wrap-params)]
            [ring.adapter.jetty :refer (run-jetty)]))

(def next-fake-id
  (let [id (atom 0)]
    (fn [] (str (swap! id inc)))))

(def the-fakest-data (atom {}))

(defn post-some-data [request]
  (let [params (:params request)
        id (next-fake-id)]
    (swap! the-fakest-data
           assoc id params)
    id))

(defn get-some-data [id]
  (str (get @the-fakest-data id)))

(defroutes app
  (GET "/" [] "Hello World")
  (POST "/data" request
        (post-some-data request))
  (GET "/data/:id" [id]
       (get-some-data id)))

(defn -main [& args]
  (run-jetty (wrap-params app)
             {:port 8080}))
