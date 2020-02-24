## happy path
* greet
  - utter_greet
* mood_great
  - utter_happy

## sad path 1
* greet
  - utter_greet
* mood_unhappy
  - utter_cheer_up
  - utter_did_that_help
* affirm
  - utter_happy

## sad path 2
* greet
  - utter_greet
* mood_unhappy
  - utter_cheer_up
  - utter_did_that_help
* deny
  - utter_goodbye

## say goodbye
* goodbye
  - utter_goodbye

## bot challenge
* bot_challenge
  - utter_iamabot

## on
* set_pin_on
    - utter_on

## off
* set_pin_off
    - utter_off

## on and off
* set_pin_on
    - utter_on
* set_pin_off
    - utter_off

## story1
* intro
    - utter_intro
* set_pin_on
    - utter_on
* set_pin_off
    -utter_off

## New Story

* greet
    - utter_greet
* set_pin_on
    - utter_on

## New Story

* greet
    - utter_greet
* intro
    - utter_intro
* set_pin_on
    - utter_on

## New Story

* set_pin_on
    - utter_on
* set_pin_on
    - utter_on
* greet
    - utter_greet
* intro
    - utter_intro
* set_pin_off
    - utter_off
* set_pin_on
    - utter_on
* goodbye
    - utter_goodbye

## New Story

* greet
    - utter_greet
* set_pin_on
    - utter_on
* set_pin_on
    - utter_on
* set_pin_on
    - utter_on
* set_pin_off
    - utter_off
* set_pin_off
    - utter_off

## New Story

* greet
    - utter_greet
* set_pin_on
    - utter_on
* set_pin_on
    - utter_on
* set_pin_off
    - utter_off
* set_pin_off
    - utter_off
* set_pin_on
    - utter_on

## New Story

* greet
    - utter_greet
* set_pin_on
    - utter_on
* set_pin_on
    - utter_on
* set_pin_off
    - utter_off
* set_pin_on{"room":"room"}
    - utter_on
* set_pin_off{"room":"room"}
    - utter_off
