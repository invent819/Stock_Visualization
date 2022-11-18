package stock.stockvisualization.domain.company;

import java.util.List;

public interface CompanyRepository {
    Company save(Company company);
    void update(Long companyId, CompanyUpdateDto updateParam);
    Company findById(Long id);
    List<Company> findAll(Company companyCond);
    void deleteById(Long id);
}
