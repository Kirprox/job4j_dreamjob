package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {

    private AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer",
                "Стажер, начинающий разработчик",
                LocalDateTime.now(), true, 2, 0));
        save(new Vacancy(0, "Junior Java Developer",
                "Младший разработчик с базовыми знаниями Java",
                LocalDateTime.now(), true, 1, 0));
        save(new Vacancy(0, "Junior+ Java Developer",
                "Младший разработчик с коммерческим опытом "
                        + "и уверенным знанием Java",
                LocalDateTime.now(), true, 3, 0));
        save(new Vacancy(0, "Middle Java Developer",
                "Разработчик среднего уровня с опытом "
                        + "участия в проектах",
                LocalDateTime.now(), true, 1, 0));
        save(new Vacancy(0, "Middle+ Java Developer",
                "Разработчик среднего уровня с лидерскими качествами "
                        + "и опытом менторства",
                LocalDateTime.now(), true, 3, 0));
        save(new Vacancy(0, "Senior Java Developer",
                "Опытный разработчик, способный принимать "
                        + "архитектурные решения",
                LocalDateTime.now(), true, 2, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(),
                        vacancy.getDescription(), vacancy.getCreationDate(),
                        vacancy.getVisible(), vacancy.getCityId(), vacancy.getFileId()
                )) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}