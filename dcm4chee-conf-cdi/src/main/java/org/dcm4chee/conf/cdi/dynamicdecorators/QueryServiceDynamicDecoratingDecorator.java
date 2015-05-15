package org.dcm4chee.conf.cdi.dynamicdecorators;

import java.util.EnumSet;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

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

@Decorator
public abstract class QueryServiceDynamicDecoratingDecorator extends DynamicDecoratorDecorator<QueryService> implements QueryService {

    @Inject
    @Delegate
    QueryService delegate;

	@Override
	public void adjustMatch(QueryContext arg0, Attributes arg1) {
		wrapWithDynamicDecorators(delegate).adjustMatch(arg0, arg1);		
	}

	@Override
	public void coerceRequestAttributes(QueryContext arg0)
			throws DicomServiceException {
		wrapWithDynamicDecorators(delegate).coerceRequestAttributes(arg0);		
	}

	@Override
	public void coerceResponseAttributes(QueryContext arg0, Attributes arg1)
			throws DicomServiceException {
		wrapWithDynamicDecorators(delegate).coerceResponseAttributes(arg0, arg1);		
	}

	@Override
	public Query createInstanceQuery(QueryContext arg0) {
		return wrapWithDynamicDecorators(delegate).createInstanceQuery(arg0);
	}

	@Override
	public Query createPatientQuery(QueryContext arg0) {
		return wrapWithDynamicDecorators(delegate).createPatientQuery(arg0);
	}

	@Override
	public Query createQuery(QueryRetrieveLevel arg0, QueryContext arg1) {
		return wrapWithDynamicDecorators(delegate).createQuery(arg0, arg1);
	}

	@Override
	public QueryContext createQueryContext(QueryService arg0) {
		return wrapWithDynamicDecorators(delegate).createQueryContext(arg0);
	}

	@Override
	public Query createSeriesQuery(QueryContext arg0) {
		return wrapWithDynamicDecorators(delegate).createSeriesQuery(arg0);
	}

	@Override
	public SeriesQueryAttributes createSeriesView(Long arg0, QueryParam arg1) {
		return wrapWithDynamicDecorators(delegate).createSeriesView(arg0, arg1);
	}

	@Override
	public Query createStudyQuery(QueryContext arg0) {
		return wrapWithDynamicDecorators(delegate).createStudyQuery(arg0);
	}

	@Override
	public StudyQueryAttributes createStudyView(Long arg0, QueryParam arg1) {
		return wrapWithDynamicDecorators(delegate).createStudyView(arg0, arg1);
	}

	@Override
	public QueryParam getQueryParam(Object arg0, String arg1,
			ArchiveAEExtension arg2, EnumSet<QueryOption> arg3, String[] arg4) {
		return wrapWithDynamicDecorators(delegate).getQueryParam(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public Attributes getSeriesAttributes(Long arg0, QueryParam arg1) {
		return wrapWithDynamicDecorators(delegate).getSeriesAttributes(arg0, arg1);
	}

	@Override
	public void initPatientIDs(QueryContext arg0) {
		wrapWithDynamicDecorators(delegate).initPatientIDs(arg0);		
	}

}
