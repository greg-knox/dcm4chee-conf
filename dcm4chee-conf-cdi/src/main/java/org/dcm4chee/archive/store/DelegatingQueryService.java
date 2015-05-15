package org.dcm4chee.archive.store;

import java.util.EnumSet;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.net.QueryOption;
import org.dcm4che3.net.service.DicomServiceException;
import org.dcm4che3.net.service.QueryRetrieveLevel;
import org.dcm4chee.archive.conf.ArchiveAEExtension;
import org.dcm4chee.archive.conf.QueryParam;
import org.dcm4chee.archive.entity.SeriesQueryAttributes;
import org.dcm4chee.archive.entity.StudyQueryAttributes;
import org.dcm4chee.archive.query.Query;
import org.dcm4chee.archive.query.QueryContext;
import org.dcm4chee.archive.query.QueryService;
import org.dcm4chee.conf.cdi.dynamicdecorators.DelegatingService;
import org.dcm4chee.conf.cdi.dynamicdecorators.DelegatingServiceImpl;

@DelegatingService
public class DelegatingQueryService extends DelegatingServiceImpl<QueryService> implements QueryService {

	@Override
	public void adjustMatch(QueryContext arg0, Attributes arg1) {
		getNextDecorator().adjustMatch(arg0, arg1);
	}

	@Override
	public void coerceRequestAttributes(QueryContext arg0)
			throws DicomServiceException {
		getNextDecorator().coerceRequestAttributes(arg0);
		
	}

	@Override
	public void coerceResponseAttributes(QueryContext arg0, Attributes arg1)
			throws DicomServiceException {
		getNextDecorator().coerceResponseAttributes(arg0, arg1);		
	}

	@Override
	public Query createInstanceQuery(QueryContext arg0) {
		return getNextDecorator().createInstanceQuery(arg0);
	}

	@Override
	public Query createPatientQuery(QueryContext arg0) {
		return getNextDecorator().createPatientQuery(arg0);
	}

	@Override
	public Query createQuery(QueryRetrieveLevel arg0, QueryContext arg1) {
		return getNextDecorator().createQuery(arg0, arg1);
	}

	@Override
	public QueryContext createQueryContext(QueryService arg0) {
		return getNextDecorator().createQueryContext(arg0);
	}

	@Override
	public Query createSeriesQuery(QueryContext arg0) {
		return getNextDecorator().createSeriesQuery(arg0);
	}

	@Override
	public SeriesQueryAttributes createSeriesView(Long arg0, QueryParam arg1) {
		return getNextDecorator().createSeriesView(arg0, arg1);
	}

	@Override
	public Query createStudyQuery(QueryContext arg0) {
		return getNextDecorator().createStudyQuery(arg0);
	}

	@Override
	public StudyQueryAttributes createStudyView(Long arg0, QueryParam arg1) {
		return getNextDecorator().createStudyView(arg0, arg1);
	}

	@Override
	public QueryParam getQueryParam(Object arg0, String arg1,
			ArchiveAEExtension arg2, EnumSet<QueryOption> arg3, String[] arg4) {
		return getNextDecorator().getQueryParam(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public Attributes getSeriesAttributes(Long arg0, QueryParam arg1) {
		return getNextDecorator().getSeriesAttributes(arg0, arg1);
	}

	@Override
	public void initPatientIDs(QueryContext arg0) {
		getNextDecorator().initPatientIDs(arg0);		
	}

}
