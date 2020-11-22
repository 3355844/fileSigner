package fileSigner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fileSigner.model.SignProcess;

public interface SignProcessRepositroy extends JpaRepository<SignProcess, Long> {

}
