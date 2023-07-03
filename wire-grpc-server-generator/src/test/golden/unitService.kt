import com.google.protobuf.DescriptorProtos
import com.google.protobuf.Descriptors
import com.squareup.wire.kotlin.grpcserver.WireBindableService
import com.squareup.wire.kotlin.grpcserver.WireMethodMarshaller
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServiceDescriptor
import io.grpc.ServiceDescriptor.newBuilder
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls.unaryRpc
import java.io.InputStream
import java.lang.Class
import java.lang.UnsupportedOperationException
import kotlin.Array
import kotlin.String
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.Set
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.Volatile

public object MyServiceWireGrpc {
  public val SERVICE_NAME: String = "MyService"

  @Volatile
  private var serviceDescriptor: ServiceDescriptor? = null

  private val descriptorMap: Map<String, DescriptorProtos.FileDescriptorProto> = mapOf(
    "service.proto" to descriptorFor(arrayOf(
      "Cg1zZXJ2aWNlLnByb3RvGhtnb29nbGUvcHJvdG9idWYvZW1wdHkucHJvdG8ySgoJTXlTZXJ2aWNlEj0K",
      "C2RvU29tZXRoaW5nEhYuZ29vZ2xlLnByb3RvYnVmLkVtcHR5GhYuZ29vZ2xlLnByb3RvYnVmLkVtcHR5",
      "YgZwcm90bzM=",
    )),
    "google/protobuf/empty.proto" to descriptorFor(arrayOf(
      "Chtnb29nbGUvcHJvdG9idWYvZW1wdHkucHJvdG8SD2dvb2dsZS5wcm90b2J1ZiIHCgVFbXB0eWIGcHJv",
      "dG8z",
    )),
  )


  @Volatile
  private var getdoSomethingMethod: MethodDescriptor<Unit, Unit>? = null

  private fun descriptorFor(`data`: Array<String>): DescriptorProtos.FileDescriptorProto {
    val str = data.fold(java.lang.StringBuilder()) { b, s -> b.append(s) }.toString()
    val bytes = java.util.Base64.getDecoder().decode(str)
    return DescriptorProtos.FileDescriptorProto.parseFrom(bytes)
  }

  private fun fileDescriptor(path: String, visited: Set<String>): Descriptors.FileDescriptor {
    val proto = descriptorMap[path]!!
    val deps = proto.dependencyList.filter { !visited.contains(it) }.map { fileDescriptor(it,
        visited + path) }
    return Descriptors.FileDescriptor.buildFrom(proto, deps.toTypedArray())
  }

  public fun getServiceDescriptor(): ServiceDescriptor? {
    var result = serviceDescriptor
    if (result == null) {
      synchronized(MyServiceWireGrpc::class) {
        result = serviceDescriptor
        if (result == null) {
          result = newBuilder(SERVICE_NAME)
          .addMethod(getdoSomethingMethod())
          .setSchemaDescriptor(io.grpc.protobuf.ProtoFileDescriptorSupplier {
                fileDescriptor("service.proto", emptySet())
              })
          .build()
          serviceDescriptor = result
        }
      }
    }
    return result
  }

  public fun getdoSomethingMethod(): MethodDescriptor<Unit, Unit> {
    var result: MethodDescriptor<Unit, Unit>? = getdoSomethingMethod
    if (result == null) {
      synchronized(MyServiceWireGrpc::class) {
        result = getdoSomethingMethod
        if (result == null) {
          getdoSomethingMethod = MethodDescriptor.newBuilder<Unit, Unit>()
            .setType(MethodDescriptor.MethodType.UNARY)
            .setFullMethodName(
              MethodDescriptor.generateFullMethodName(
                "MyService", "doSomething"
              )
            )
            .setSampledToLocalTracing(true)
            .setRequestMarshaller(MyServiceImplBase.EmptyMarshaller())
            .setResponseMarshaller(MyServiceImplBase.EmptyMarshaller())
            .build()
        }
      }
    }
    return getdoSomethingMethod!!
  }

  public fun newStub(channel: Channel): MyServiceStub = MyServiceStub(channel)

  public abstract class MyServiceImplBase(
    protected val context: CoroutineContext = kotlin.coroutines.EmptyCoroutineContext
    ,
  ) : WireBindableService {
    public open suspend fun doSomething(request: Unit): Unit = throw UnsupportedOperationException()

    override fun bindService(): ServerServiceDefinition =
        ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(
               io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition(
                 context = context,
                 descriptor = getdoSomethingMethod(),
                 implementation = this@MyServiceImplBase::doSomething,
               )
             ).build()

    public class EmptyMarshaller : WireMethodMarshaller<Unit> {
      override fun stream(`value`: Unit): InputStream =
          com.squareup.wire.ProtoAdapter.EMPTY.encode(value).inputStream()

      override fun marshalledClass(): Class<Unit> = Unit::class.java

      override fun parse(stream: InputStream): Unit =
          com.squareup.wire.ProtoAdapter.EMPTY.decode(stream)
    }
  }

  public class BindableAdapter(
    private val service: () -> MyServiceServer,
  ) : MyServiceImplBase() {
    override suspend fun doSomething(request: Unit): Unit = service().doSomething(request)
  }

  public class MyServiceStub : AbstractCoroutineStub<MyServiceStub> {
    internal constructor(channel: Channel) : super(channel)

    internal constructor(channel: Channel, callOptions: CallOptions) : super(channel, callOptions)

    override fun build(channel: Channel, callOptions: CallOptions): MyServiceStub =
        MyServiceStub(channel, callOptions)

    public suspend fun doSomething(request: Unit): Unit = unaryRpc(channel, getdoSomethingMethod(),
        request, callOptions)
  }
}
