package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.repository.Sql2oVacancyRepository;
import ru.job4j.dreamjob.repository.VacancyRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleVacancyService implements VacancyService {
    private final VacancyRepository vacancyRepository;

    private final FileService fileService;

    public SimpleVacancyService(Sql2oVacancyRepository vacancyRepository, FileService fileService) {
        this.vacancyRepository = vacancyRepository;
        this.fileService = fileService;
    }

    @Override
    public Vacancy save(Vacancy vacancy, FileDto image) {
        saveNewFile(vacancy, image);
        vacancy.setCreationDate(LocalDateTime.now());
        return vacancyRepository.save(vacancy);
    }

    private void saveNewFile(Vacancy vacancy, FileDto image) {
        var file = fileService.save(image);
        vacancy.setFileId(file.getId());
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted = false;
        var fileOptional = findById(id);
        if (fileOptional.isPresent()) {
            isDeleted = vacancyRepository.deleteById(id);
            fileService.deleteById(fileOptional.get().getFileId());
        }
        return isDeleted;
    }

    @Override
    public boolean update(Vacancy vacancy, FileDto image) {
        var isNewFileEmpty = image.getContent().length == 0;
        vacancy.setCreationDate(LocalDateTime.now());
        if (isNewFileEmpty) {
            return vacancyRepository.update(vacancy);
        }
        /* если передан новый не пустой файл, то старый удаляем, а новый сохраняем */
        var oldFileId = vacancy.getFileId();
        saveNewFile(vacancy, image);
        var isUpdated = vacancyRepository.update(vacancy);
        fileService.deleteById(oldFileId);
        return isUpdated;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return vacancyRepository.findById(id);
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }
}