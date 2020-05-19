package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.FilterTemplateService;
import com.arimac.backend.pmtool.projectmanagementtool.repository.FilterTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class FilterTemplateServiceImpl implements FilterTemplateService {

    private final FilterTemplateRepository filterTemplateRepository;

    public FilterTemplateServiceImpl(FilterTemplateRepository filterTemplateRepository) {
        this.filterTemplateRepository = filterTemplateRepository;
    }

    @Override
    public Object createFilterTemplate() {
//        filterTemplateRepository.createFilterTemplate();
        return null;
    }
}
