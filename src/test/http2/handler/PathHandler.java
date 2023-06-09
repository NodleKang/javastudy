package test.http2.handler;

import org.eclipse.jetty.util.IO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PathHandler implements Runnable {

        private HttpServletRequest req;
        private HttpServletResponse resp;

        public PathHandler(HttpServletRequest req, HttpServletResponse resp) {
                this.req = req;
                this.resp = resp;
        }

        private void process(Reader in) throws IOException {
                try {
                        in.read();
                } catch (Throwable th) {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.setHeader("Content-Type", "application/json");
                        resp.getWriter().write("{\"result\":\"ERROR\"}");
                } finally {
                        resp.setHeader("Content-Type", "application/json");
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("{\"result\":\"OK\"}");
                        resp.getWriter().println("result text");
                        IO.close(in);
                }
        }

        @Override
        public void run() {
                try {
                        process(req.getReader());
                } catch (IOException e) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                }
        }
}
