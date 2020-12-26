(ns techshop.core-test
  (:require [clojure.test :refer :all]
            [techshop.core :refer :all]))

(defn create-product [arg] (
  (if (= arg nil) (throw (NullPointerException.))
    (if (not (:name arg)) (throw (Exception. "requires name property")))
  )
))


(deftest product-manager-suite
  (testing "cannot create nil product"
    (is (thrown? NullPointerException (create-product nil)) )
  )
  (testing "requires a name property"
    (is (thrown-with-msg? Exception #"requires name property"
                          (create-product {:notaname ""})))
  )
)