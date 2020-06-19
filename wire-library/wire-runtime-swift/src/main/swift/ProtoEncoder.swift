//
//  Copyright © 2020 Square Inc. All rights reserved.
//

import Foundation

/**
 A class responsible for turning an in-memory struct generated by the Wire
 code generator into serialized bytes in the protocol buffer format that 
 represent that struct.

 General usage will look something like:
 ```
 let encoder = ProtoEncoder()
 let data = try encoder.encode(generatedMessageInstance)
 ```
 */
public final class ProtoEncoder {

    // MARK: -

    public enum Error: Swift.Error, LocalizedError {

        case stringNotConvertibleToUTF8(String)

        var localizedDescription: String {
            switch self {
            case let .stringNotConvertibleToUTF8(string):
                return "The string \"\(string)\" could not be converted to UTF8."
            }
        }

    }

    // MARK: -

    /// The formatting of the output binary data.
    public struct OutputFormatting : OptionSet {

        /// The format's numerical value.
        public let rawValue: UInt

        /// Creates an OutputFormatting value with the given raw value.
        public init(rawValue: UInt) {
            self.rawValue = rawValue
        }

        /**
         Produce serialized data with map keys sorted in comparable order.
         This is useful for creating deterministic data output but incurs a minor
         performance penalty and is not usually necessary in production use cases.
         */
        public static let sortedKeys: OutputFormatting = .init(rawValue: 1 << 1)

    }

    // MARK: - Properties

    public var outputFormatting: OutputFormatting = []

    // MARK: - Initialization

    public init() {}

    // MARK: - Public Methods

    public func encode<T: ProtoEncodable>(_ value: T) throws -> Data {
        fatalError("TODO")
    }

}
