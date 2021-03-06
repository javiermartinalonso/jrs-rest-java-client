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

package com.jaspersoft.jasperserver.jaxrs.client.apiadapters.reporting.reportparameters;

import com.jaspersoft.jasperserver.dto.reports.ReportParameters;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.AbstractAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.*;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.handling.DefaultErrorHandler;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.inputcontrols.InputControlStateListWrapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import static com.jaspersoft.jasperserver.jaxrs.client.core.JerseyRequest.buildRequest;

/**
 * @deprecated Replaced by {@link com.jaspersoft.jasperserver.jaxrs.client.apiadapters.inputControls.InputControlsValuesAdapter}.
 */
public class ReportParametersValuesAdapter extends AbstractAdapter {

    protected final String reportUnitUri;
    protected MultivaluedMap<String, String> params;
    private String idsPathSegment;

    public ReportParametersValuesAdapter(SessionStorage sessionStorage, String reportUnitUri) {
        super(sessionStorage);
        this.reportUnitUri = reportUnitUri;
    }

    public ReportParametersValuesAdapter(SessionStorage sessionStorage, String reportUnitUri, String idsPathSegment,
                                         MultivaluedMap<String, String> params) {
        this(sessionStorage, reportUnitUri);
        this.idsPathSegment = idsPathSegment;
        this.params = params;
    }

    public OperationResult<InputControlStateListWrapper> get() {
        JerseyRequest<InputControlStateListWrapper> request = prepareRequest();
        return request.post(ReportParametersUtils.toReportParameters(params));
    }

    public <R> RequestExecution asyncGet(final Callback<OperationResult<InputControlStateListWrapper>, R> callback) {
        final JerseyRequest<InputControlStateListWrapper> request = prepareRequest();
        final ReportParameters reportParameters = ReportParametersUtils.toReportParameters(params);

        RequestExecution task = new RequestExecution(new Runnable() {
            @Override
            public void run() {
                callback.execute(request.post(reportParameters));
            }
        });

        ThreadPoolUtil.runAsynchronously(task);
        return task;
    }

    private JerseyRequest<InputControlStateListWrapper> prepareRequest() {
        JerseyRequest<InputControlStateListWrapper> request =
                buildRequest(sessionStorage, InputControlStateListWrapper.class,
                        new String[]{"/reports", reportUnitUri, "/inputControls"}, new DefaultErrorHandler());
        if (idsPathSegment != null) {
            request.setPath(idsPathSegment);
        }
        request.setPath("values");
        request.setContentType(MediaType.APPLICATION_XML);
        request.setAccept(MediaType.APPLICATION_XML);

        return request;
    }

}
