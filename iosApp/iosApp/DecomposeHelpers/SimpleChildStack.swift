//
//  SimpleChildStack.swift
//  iosApp
//
//  Created by Pavel on 26.04.2023.
//  Copyright © 2023 orgName. All rights reserved.
//


import HieroUi

func simpleChildStack<T : AnyObject>(_ child: T) -> Value<ChildStack<AnyObject, T>> {
    return mutableValue(
        ChildStack(
            configuration: "config" as AnyObject,
            instance: child
        )
    )
}
