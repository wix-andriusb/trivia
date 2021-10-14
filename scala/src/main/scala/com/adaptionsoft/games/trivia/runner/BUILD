load("@io_bazel_rules_scala//scala:scala.bzl", "scala_binary", "scala_library", "scala_test")
package(default_visibility = ["//visibility:public"])

scala_binary(
    name = "runner",
    srcs = ["GameRunner.scala"],
    deps = [
        "//src/main/scala/com/adaptionsoft/games/uglytrivia"
    ],
    main_class = "com.adaptionsoft.games.trivia.runner.GameRunner")