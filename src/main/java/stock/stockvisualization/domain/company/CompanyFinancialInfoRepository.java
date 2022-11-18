package stock.stockvisualization.domain.company;

public interface CompanyFinancialInfoRepository {
    CompanyFinancialInfo save(CompanyFinancialInfo company);
    CompanyFinancialInfo update(CompanyFinancialInfo company);
    CompanyFinancialInfo findByCompanyId(Long id);
    void deleteById(Long id);
}
