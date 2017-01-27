@GrabResolver(name = "OJO", root = "https://oss.jfrog.org/artifactory/repo")
@Grab("com.couchbase.client:java-client:2.3.3")
@Grab("org.assertj:assertj-core:2.5.0")
@Grab("io.reactivex:rxgroovy:1.0.3")
@GrabConfig(systemClassLoader = true)
import com.couchbase.client.java.document.RawJsonDocument
import com.couchbase.client.java.document.JsonDocument
import com.couchbase.client.core.RequestCancelledException
import com.couchbase.client.core.BackpressureException
import com.couchbase.client.java.error.TemporaryFailureException
import com.couchbase.client.java.util.retry.RetryBuilder
import groovy.json.JsonSlurper
import rx.Observable
import com.couchbase.client.java.CouchbaseCluster
import rx.functions.Action1
import rx.functions.Func1

import java.nio.file.Path
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream
import java.util.stream.Stream
import java.nio.file.Files

import static com.couchbase.client.core.time.Delay.fixed

class CBImporter {

    public static void main(String[] args) {

        def jsonSlurper = new JsonSlurper()
        def cluster = CouchbaseCluster.create(args[1])
        def asyncBucket = cluster.openBucket(args[2]).async()
        FileWriter successFileWriter = new FileWriter("success.out", true)
        FileWriter errorFileWriter = new FileWriter("error.out", true)
        File f = new File(args[0])

        Stream<String> stream = lines(f.toPath())

        Iterable iterable = new Iterable() {
            @Override
            Iterator iterator() {
                return stream.iterator()
            }
        }

        Observable.from(iterable)
                .flatMap({
            String s ->
                def json = jsonSlurper.parseText(s)
                Observable.just(RawJsonDocument.create(json.id, s))
        } as Func1<String, Observable<RawJsonDocument>>
        )
                .flatMap({
            RawJsonDocument jsonDoc ->
                asyncBucket.upsert(jsonDoc)
                        .retryWhen(
                        RetryBuilder
                                .anyOf(RequestCancelledException.class)
                                .delay(fixed(500,
                                TimeUnit.MILLISECONDS))
                                .max(50).build())
                        .retryWhen(
                        RetryBuilder
                                .anyOf(TemporaryFailureException.class,
                                BackpressureException.class)
                                .delay(fixed(500,
                                TimeUnit.MILLISECONDS))
                                .max(50).build())
                        .doOnNext({ jd ->  successFileWriter.write(jsonDoc.id() + System.getProperty("line.separator")) } as Action1<RawJsonDocument>)
                        .doOnError({ t ->  errorFileWriter.write(jsonDoc.id() + System.getProperty("line.separator")) } as Action1<Throwable>)
                        .onErrorResumeNext({ t -> println t; Observable.empty() } as Func1<Throwable, Observable>)
        } as Func1<RawJsonDocument, Observable<JsonDocument>>).toBlocking().last()
        successFileWriter.close()
        errorFileWriter.close()
    }

    public static Stream<String> lines(Path path) {
        InputStream fileIs = null;
        BufferedInputStream bufferedIs = null;
        GZIPInputStream gzipIs = null;
        try {
            fileIs = Files.newInputStream(path);
            // Even though GZIPInputStream has a buffer it reads individual bytes
            // when processing the header, better add a buffer in-between
            bufferedIs = new BufferedInputStream(fileIs, 65535);
            gzipIs = new GZIPInputStream(bufferedIs);
        } catch (IOException e) {
            closeSafely(gzipIs);
            closeSafely(bufferedIs);
            closeSafely(fileIs);
            throw new UncheckedIOException(e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(gzipIs));
        return reader.lines().onClose({ closeSafely(reader) });
    }

    private static void closeSafely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

}