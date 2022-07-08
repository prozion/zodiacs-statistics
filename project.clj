(defproject zodiacs-statistics "0.0.2"
  :description "some description"
  :url "https://github.com/prozion/zodiacs-statistics"
  :license {:name "MIT License"
            :url  "https://github.com/aws/mit-0"}
  :dependencies [
                [org.clojars.prozion/odysseus "0.1.6"]
                [cheshire "5.11.0"]
                ]
  :plugins [
            [lein-ancient "0.6.15"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]]}}
  :repl-options {
    :init-ns scripts.analyze
  }
)
