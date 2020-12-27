(ns techshop.core-test
  (:require [clojure.test :refer :all]
            [techshop.core :refer :all]))

(defprotocol ProductRepository
  (save [this product]))

(def saved-product (atom nil))
(deftype InMemoryProductRepository []
  ProductRepository
  (save [this product] (swap! saved-product (fn [] product))))

(defprotocol ProductSaver
  (save-product [this record]))

(deftype SaveProductCommand [product-repository]
  ProductSaver
  (save-product [this record] (
      (if (not (map? record)) (throw (Exception. "product must be a map")))
      (if (empty? record) (throw (Exception. "argument is empty")))
      (if (not (:name record)) (throw (Exception. "missing name property")))
      (if (empty? (:name record)) (throw (Exception. "name is empty")))
  )))

(deftest save-product-suite
  (let [cmd (SaveProductCommand. (InMemoryProductRepository.)) ]
    (testing "product must be a map"
      (let [regex #"product must be a map"]
        (is (thrown-with-msg? Exception regex (save-product cmd nil)))
        (is (thrown-with-msg? Exception regex (save-product cmd "")))
        (is (thrown-with-msg? Exception regex (save-product cmd [])))
      )
    )
    (testing "throws on empty argument"
      (is (thrown-with-msg? Exception #"argument is empty" (save-product cmd {})))
    )
    (testing "throws if arg missing name property"
      (is (thrown-with-msg? Exception #"missing name property"
                            (save-product cmd {:stock 5})))
    )
    (testing "throws if name is empty"
      (is (thrown-with-msg? Exception #"name is empty"
                            (save-product cmd {:name "" :stock 5})))
      )
    ;(testing "name but no stock, leaves stock as 0 by default"
    ;
    ;  (let [cmd (SaveProductCommand. (SpyProductRepo.))] (is
    ;     (do
    ;       (save-product cmd {:name "Macaroni"})
    ;       )
    ;     ))
    ;)
  )
)