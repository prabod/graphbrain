(ns graphbrain.gbui.item
  (:require-macros [hiccups.core :as hiccups])
  (:require [jayq.core :as jq]
            [graphbrain.gbui.remove :as rem]
            [graphbrain.gbui.globals :as g]
            [hiccups.runtime :as hiccupsrt])
  (:use [jayq.core :only [$]]))

(defn item-div-id
  [edge]
  (if (empty? edge)
    "i_"
    (let [div-id (clojure.string/replace edge #"\W" "_")]
      (str "i_" div-id))))

(hiccups/defhtml url-item-html
  [node div-id]
  [:div {:class "item-main"}
   [:div {:class "item-url-title" :id (str "t" div-id)}
    [:a {:href (str "/x/" (js/encodeURIComponent (:id node)))
         :id div-id}
     (:text node)]]
   [:br]
   (if (not= (:icon node) "")
     [:div {:class "item-ico"}
      [:img {:src (:icon node) :width "16px" :height "16px" :class "item-ico"}]])
   [:div {:class "item-url"}
    [:a {:href (:url node) :id (str "url" div-id)}
     (:url node)]]]
  [:div {:class "node-remove"}
   [:a {:id (str "rem" div-id) :href "#"} "x"]]
  [:div {:style "clear:both;"}])

(defn- subtext-item
  [sub]
  (if (= (:id sub) "")
    (:text sub)
    (str "<a href='/x/" (:id sub) "'>" (:text sub) "</a>")))

(defn- subtext
  [sub]
  (clojure.string/join ", " (map subtext-item sub)))

(hiccups/defhtml entity-item-html
  [node div-id sub-txt]
  [:div {:class "item-main"}
   [:div {:class "item-title" :id (str "t" div-id)}
    [:a {:href (str "/x/" (js/encodeURIComponent (:id node))) :id div-id}
     (str (:text node) "&nbsp;")]]
   (if sub-txt
     [:div {:class "item-sub-text"} sub-txt])]
  [:div {:class "item-remove"}
   [:a {:id (str "rem" div-id) :href "#"} "x"]]
  [:div {:style "clear:both;"}])

(defn- item-html
  [item div-id]
  (let [type (:type item)]
    (if (= type "url")
      (url-item-html item div-id)
      (entity-item-html item div-id (subtext (:sub item))))))

(defn- item-color
  [item ctxts]
  (:color (ctxts (first (:ctxts item)))))

(defn item-place
  [item frame-id snode ctxts]
  (let [class "item"
        div-id (item-div-id (:edge item))
        html (str "<div id='" div-id "' class='" class "'>")
        html (str html (item-html item div-id))
        html (str html "</div>")]
    (jq/append ($ (str "#" frame-id " .viewport")) html)
    (jq/css ($ (str "#t" div-id)) {:background (item-color item ctxts)})
    (jq/bind ($ (str "#rem" div-id))
                   :click
                   #(rem/remove-clicked item snode))
    item))