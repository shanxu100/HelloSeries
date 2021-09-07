aux_source_directory(. RTSA_JVM_SRC_LIST)
add_library(jvm-lib STATIC ${RTSA_JVM_SRC_LIST})
target_link_libraries( # Specifies the target library.
        jvm-lib
        logpringt-lib
        )