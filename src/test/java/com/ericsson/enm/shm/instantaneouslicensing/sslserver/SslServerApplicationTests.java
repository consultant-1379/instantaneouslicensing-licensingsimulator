/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.enm.shm.instantaneouslicensing.sslserver;

import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_11_FORCED_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.ElisRequest;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.ElisResponseId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = SslServerApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SslServerApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(SslServerApplicationTests.class);

    private RestTemplate template;

    private ElisRequest elis;
    private Gson gson;

    @Mock
    ResponseEntity responseEntity;

    @Autowired
    private MockMvc mvc;

    @LocalServerPort
    private int randomServerPort;

    @Before
    public void init() throws IOException {

        final String elisJson = "{\n"
                                + "    \"euft\":\"euft1\",\n"
                                + "    \"type\":\"refresh\",\n"
                                + "    \"swdxId\":\"1234\",\n"
                                + "    \"globalCustomerId\":\"21\",\n"
                                + "    \"destination\":\"/store/LicenseFiles\",\n"
                                + "    \"nodes\":[{\"nodeType\":\"5G\",\"fingerprint\":\"6543\",\"swRelease\":\"1.0\",\"swltId"
                                + "\":\"111\",\"capacities\":[{\"keyId\":\"3489\","
                                + "\"requiredLevel\":\"2\"}]}]\n"
                                + "}";
        gson = new Gson();
        elis = gson.fromJson(elisJson, ElisRequest.class);
        try (
                final InputStream elisCertifcate = new FileInputStream(ConstantValue.ELIS_SIM_ROOT_CA)) {
            final KeyStore elisKeyStore = KeyStore.getInstance(ConstantValue.KEYSTORE_TYPE_PKCS12);
            elisKeyStore.load(elisCertifcate, ConstantValue.KEYSTORE_PW);
            final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(elisKeyStore, new TrustSelfSignedStrategy()).build();
            final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
                    new String[] { ConstantValue.SUPPORTED_PROTOCOL_TLS_V_1_2 },
                    null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            final HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            elisCertifcate.close();
            this.template = new RestTemplate(requestFactory);
        } catch (final KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.error(e.getMessage());
        } catch (final FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (final CertificateException e) {
            logger.error(e.getMessage());
        } catch (final IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void testRequestFromElisRandoumId() throws Exception {
        final String randomId = requestFromElis("randomId");
        assertNotNull(randomId);

    }

    @Test
    public void testBadRequestFromElis() throws Exception {
        final String baseUrl = "https://localhost:" + randomServerPort + "/requestIdFromElis/api/v1/licenses";
        final URI uri = new URI(baseUrl);
        final ElisRequest elisRequest = getElisRequest("euft10");

        final String jsonFirstRequest = gson.toJson(elisRequest);
        System.out.println("Json " + jsonFirstRequest);
        try {
            this.mvc.perform(MockMvcRequestBuilders.post("https://localhost:" + randomServerPort + "/requestIdFromElis/api/v1"
                                                         + "/licenses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(getHttpHeader())
                    .content(jsonFirstRequest))
                    .andExpect(status().isBadRequest());
        } catch (final HttpClientErrorException ex) {
            //Verify bad request
            Assert.assertEquals(400, ex.getRawStatusCode());
            Assert.assertEquals(true, ex.getResponseBodyAsString().contains("EUFT value is null"));
            Assert.assertEquals(true, ex.getResponseBodyAsString().contains("Validation failure"));
        }
    }

    @Test
    public void testInternalServerErrorFromElis() throws Exception {
        final String baseUrl = "https://localhost:" + randomServerPort + "/requestIdFromElis/api/v1/licenses";
        final URI uri = new URI(baseUrl);
        final ElisRequest elisRequest = new ElisRequest();
        elisRequest.setEuft(EUFT_11_FORCED_INTERNAL_SERVER_ERROR);
        elisRequest.setDestination("destination");
        elisRequest.setGlobalCustomerId("12");
        elisRequest.setSwdxId("43");
        elisRequest.setType("refresh");
        final String jsonFirstRequest = gson.toJson(elisRequest);
        try {
            this.mvc.perform(MockMvcRequestBuilders.post("https://localhost:" + randomServerPort + "/requestIdFromElis/api/v1"
                                                         + "/licenses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(getHttpHeader())
                    .content(jsonFirstRequest))
                    .andExpect(status().is5xxServerError());

        } catch (final HttpServerErrorException ex) {
            Assert.assertEquals(500, ex.getRawStatusCode());
            Assert.assertEquals(true, ex.getResponseBodyAsString().contains("Internal Server Error"));
            Assert.assertEquals(true, ex.getResponseBodyAsString().contains("Server unavailable"));
        }
    }

    private ElisResponseId sendRequestIdFromElis(final ElisRequest elisRequest) throws Exception {
        ElisResponseId result = new ElisResponseId();
        final String baseUrl = "https://localhost:" + randomServerPort + "/requestIdFromElis/api/v1/licenses";
        final URI uri;
        final String jsonFirstRequest = gson.toJson(elisRequest);
        try {
            uri = new URI(baseUrl);
            final MvcResult response = this.mvc.perform(MockMvcRequestBuilders.post(
                    "https://localhost:" + randomServerPort + "/requestIdFromElis/api/v1"
                    + "/licenses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(getHttpHeader())
                    .content(jsonFirstRequest))
                    .andReturn();
            final Gson gson = new Gson();
            result = gson.fromJson(response.getResponse().getContentAsString(), ElisResponseId.class);
            logger.info("Request Json object: {}", jsonFirstRequest);
        } catch (final URISyntaxException | JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    private String requestFromElis(final String euft) throws Exception {
        String resquestId = " ";
        final ElisRequest elisRequest = getElisRequest(euft);
        final ElisResponseId response = sendRequestIdFromElis(elisRequest);
        resquestId = response.getId();
        return resquestId;
    }

    @Test
    @SuppressWarnings("squid:S2925")
    public void issue_License_Requests_Should_Return_Accepted() throws Exception {
        final String jsonFirstRequest = gson.toJson(elis);

        this.mvc.perform(MockMvcRequestBuilders.post("https://localhost:" + randomServerPort + "/requestIdFromElis/api/v1/licenses")
                .headers(getHttpHeader())
                .content(jsonFirstRequest))
                .andExpect(status().isAccepted());
        Thread.sleep(1000);//Sleeping because the controller is async for the test to complete

    }

    private HttpHeaders getHttpHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/vnd.ericsson.eth.v2.0.0+json");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.ericsson.eth.v2.0.0+json");
        return headers;
    }

    private ElisRequest getElisRequest(final String euft) {
        final ElisRequest elisRequest = new ElisRequest();
        elisRequest.setEuft(euft);
        elisRequest.setDestination("destination");
        elisRequest.setGlobalCustomerId("12");
        elisRequest.setSwdxId("43");
        elisRequest.setType("refresh");
        final List<Node> nodeList = new ArrayList<>();
        final Node node = new Node();
        node.setFingerprint("node");
        node.setNodeType("5G");
        node.setSwltId("111");
        node.setSwRelease("20.04");
        final Node node1 = new Node();
        node1.setFingerprint("node1");
        node1.setNodeType("5G");
        node1.setSwltId("222");
        node1.setSwRelease("20.04");
        nodeList.add(node);
        nodeList.add(node1);
        elisRequest.setNodes(nodeList);
        return elisRequest;
    }
}