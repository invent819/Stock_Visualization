package stock.stockvisualization.domain.company;

public interface CompanyRepository {
    Company save(Company company);
    Company update(Company company);
    Company findById(Long id);
    void deleteById(Long id);

}
