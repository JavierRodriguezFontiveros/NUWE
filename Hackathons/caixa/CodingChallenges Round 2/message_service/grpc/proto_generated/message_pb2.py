# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: message.proto
# Protobuf Python Version: 5.29.0
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    29,
    0,
    '',
    'message.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import timestamp_pb2 as google_dot_protobuf_dot_timestamp__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\rmessage.proto\x12\x0fmessage_service\x1a\x1fgoogle/protobuf/timestamp.proto\"\x8b\x01\n\x07Message\x12\x12\n\nmessage_id\x18\x01 \x01(\t\x12\x14\n\x0csender_email\x18\x02 \x01(\t\x12\x16\n\x0ereceiver_email\x18\x03 \x01(\t\x12\x0f\n\x07\x63ontent\x18\x04 \x01(\t\x12-\n\ttimestamp\x18\x05 \x01(\x0b\x32\x1a.google.protobuf.Timestamp\"?\n\x12SendMessageRequest\x12)\n\x07message\x18\x01 \x01(\x0b\x32\x18.message_service.Message\">\n\x13SendMessageResponse\x12\x0f\n\x07success\x18\x01 \x01(\x08\x12\x16\n\x0estatus_message\x18\x02 \x01(\t\"I\n\x12GetMessagesRequest\x12\x12\n\nuser_email\x18\x01 \x01(\t\x12\x0c\n\x04page\x18\x02 \x01(\x05\x12\x11\n\tpage_size\x18\x03 \x01(\x05\"A\n\x13GetMessagesResponse\x12*\n\x08messages\x18\x01 \x03(\x0b\x32\x18.message_service.Message2\xc4\x01\n\x0eMessageService\x12X\n\x0bSendMessage\x12#.message_service.SendMessageRequest\x1a$.message_service.SendMessageResponse\x12X\n\x0bGetMessages\x12#.message_service.GetMessagesRequest\x1a$.message_service.GetMessagesResponseb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'message_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  DESCRIPTOR._loaded_options = None
  _globals['_MESSAGE']._serialized_start=68
  _globals['_MESSAGE']._serialized_end=207
  _globals['_SENDMESSAGEREQUEST']._serialized_start=209
  _globals['_SENDMESSAGEREQUEST']._serialized_end=272
  _globals['_SENDMESSAGERESPONSE']._serialized_start=274
  _globals['_SENDMESSAGERESPONSE']._serialized_end=336
  _globals['_GETMESSAGESREQUEST']._serialized_start=338
  _globals['_GETMESSAGESREQUEST']._serialized_end=411
  _globals['_GETMESSAGESRESPONSE']._serialized_start=413
  _globals['_GETMESSAGESRESPONSE']._serialized_end=478
  _globals['_MESSAGESERVICE']._serialized_start=481
  _globals['_MESSAGESERVICE']._serialized_end=677
# @@protoc_insertion_point(module_scope)
