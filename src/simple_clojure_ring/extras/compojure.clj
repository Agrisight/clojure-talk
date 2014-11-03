(ns simple-clojure-ring.extras.compojure
  (:require [compojure.core :refer (defroutes GET POST)]
            [ring.middleware.params :refer (wrap-params)]
            [ring.adapter.jetty :refer (run-jetty)]))

(def next-fake-id
  (let [id (atom 0)]
    (fn []
      (str (swap! id inc)))))

(def the-fakest-data (atom {}))

(defroutes app
  (GET "/" [] "Hello World")
  (POST "/data" request
        (let [params (:params request)
              id (next-fake-id)]
          (swap! the-fakest-data
                 assoc id params)
          id))
  (GET "/data/:id" [id]
       (str (get @the-fakest-data id))))

(defn -main [& args]
  (run-jetty (wrap-params app)
             {:port 8080}))
