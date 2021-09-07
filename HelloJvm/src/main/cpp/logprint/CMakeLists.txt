aux_source_directory(. ALL_SRC_LIST)
add_library(logpringt-lib STATIC ${ALL_SRC_LIST})
target_link_libraries( # Specifies the target library.
        logpringt-lib
        )