/*
 * Copyright (C) 2005 - 2014 Jaspersoft Corporation. All rights  reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program.&nbsp; If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.jasperserver.jaxrs.client.restservices;

import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.importexport.exportservice.ExportParameter;
import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

public class ExportServiceTest extends Assert {

    private static JasperserverRestClient client;
    private StateDto stateDto;

    @BeforeClass
    public static void setUp() {
        RestClientConfiguration configuration = RestClientConfiguration.loadConfiguration("url.properties");
        client = new JasperserverRestClient(configuration);
    }

    @Test
    public void testCreateExportTask() {
        OperationResult<StateDto> operationResult =
                client
                        .authenticate("superuser", "superuser")
                        .exportService()
                        .newTask()
                        .user("jasperadmin")
                        .role("ROLE_USER")
                        .parameter(ExportParameter.EVERYTHING)
                        .create();

        stateDto = operationResult.getEntity();

        assertNotEquals(stateDto, null);
    }

    @Test(dependsOnMethods = {"testCreateExportTask"})
    public void testCreateExportTaskAndGet() {
        OperationResult<StateDto> operationResult =
                client
                        .authenticate("superuser", "superuser")
                        .exportService()
                        .task(stateDto.getId())
                        .state();

        StateDto state = operationResult.getEntity();
        assertNotEquals(state, null);
    }

    @Test(dependsOnMethods = {"testCreateExportTask", "testCreateExportTaskAndGet"})
    public void testGetExportInputStream() throws InterruptedException, IOException {
        OperationResult<InputStream> operationResult1 =
                client
                        .authenticate("superuser", "superuser")
                        .exportService()
                        .task(stateDto.getId())
                        .fetch();

        InputStream inputStream = operationResult1.getEntity();
        assertNotEquals(inputStream, null);
        inputStream.close();
    }

}
