package it.unisa.di.tirociniosmart.domandetirocinio;

import it.unisa.di.tirociniosmart.progettiformativi.ProgettoFormativo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class DomandeTirocinioService {
  
  @Autowired
  private DomandaTirocinioRepository domandaRepository;
  
  /**
   * Permette di richiedere al sistema il salvataggio di una domanda di tirocinio. 
   * 
   * @param domanda {@link DomandaTirocinio} per cui si vuole registrare una domanda di tirocinio.
   *                 Non è necessario specificare la data della domanda di tirocinio ad essa
   *                 associata poiché è il metodo stesso ad impostarla.
   * 
   * @pre domanda != null
   */
  @Transactional
  public void registraDomandaTirocinio(DomandaTirocinio domanda) throws Exception {
    //valida i campi dello studente
    domanda.setInizioTirocinio(validaDataDiInizioTirocinio(domanda.getInizioTirocinio(),
                                                           domanda.getFineTirocinio()));
    domanda.setFineTirocinio(validaDataDiFineTirocinio(domanda.getInizioTirocinio(),
                                                       domanda.getFineTirocinio()));
    domanda.setCfu(validaCfu(domanda.getCfu()));
    domanda.setCommentoStudente(validaCommento(domanda.getCommentoStudente()));
    domanda.setProgettoFormativo(verificaStatoProgettoFormativo(domanda.getProgettoFormativo()));
    
    // Imposta stato e data della domanda
    domanda.setStatus(DomandaTirocinio.IN_ATTESA);
    domanda.setData(LocalDateTime.now());
    
    // Registra le informazioni
    domandaRepository.save(domanda);
    
  }
  
  /**
   * Permette ad un delegato aziendale di accettare una domanda di tirocinio già presente nel 
   * sistema.
   * 
   * @param idDomanda Long che rappresenta l'identificatore della domanda di tirocinio
   *                  da accettare
   * 
   * @throws IdDomandaTirocinioNonValidoException se non esiste alcuna domanda di
   *         tirocinio nel sistema con identificatore uguale ad idDomanda
   * 
   * @throws DomandaTirocinioGestitaException se la domanda identificata da idDomanda
   *         si trova in uno stato diverso da quello in attesa
   */
  @Transactional
  public void accettaDomandaTirocinio(long idDomanda)
         throws IdDomandaTirocinioNonValidoException,
                DomandaTirocinioGestitaException {
    // Controlla che la domanda esista
    DomandaTirocinio domanda = domandaRepository.findById(idDomanda);
    if (domanda == null) {
      throw new IdDomandaTirocinioNonValidoException();
    }
    
    // Controlla che la domanda non sia già stata gestita in precedenza
    if (domanda.getStatus() != DomandaTirocinio.IN_ATTESA) {
      throw new DomandaTirocinioGestitaException();
    } else {
      domanda.setStatus(DomandaTirocinio.ACCETTATA);
    }
  }
  
  
  /**
   * Permette ad un delegato aziendale di rifiutare una domanda di tirocinio già presente nel 
   * sistema.
   * 
   * @param idDomanda Long che rappresenta l'identificatore della domanda di tirocinio
   *                  da rifiutare
   * 
   * @throws IdDomandaTirocinioNonValidoException se non esiste alcuna domanda di tirocinio
   *         nel sistema con identificatore uguale ad idDomanda
   * 
   * @throws DomandaTirocinioGestitaException se la domanda identificata da idDomanda
   *         si trova in uno stato diverso da quello in attesa
   *         
   * @throws CommentoDomandaTirocinioNonValidoException se il commento da associare alla
   *         domanda è nullo o vuoto
   */
  @Transactional
  public void rifiutaDomandaTirocinio(long idDomanda, String commento)
         throws IdDomandaTirocinioNonValidoException,
                DomandaTirocinioGestitaException,
                CommentoDomandaTirocinioNonValidoException {
    // Controlla che la domanda esista
    DomandaTirocinio domanda = domandaRepository.findById(idDomanda);
    if (domanda == null) {
      throw new IdDomandaTirocinioNonValidoException();
    }
    
    // Controlla che la richiesta non sia già stata gestita in precedenza ed impostane lo stato
    if (domanda.getStatus() != DomandaTirocinio.IN_ATTESA) {
      throw new DomandaTirocinioGestitaException();
    } else {
      domanda.setStatus(DomandaTirocinio.RIFIUTATA);  
    }
    domanda.setCommentoAzienda(validaCommento(commento));
  }
  
  /**
   * Permette ad un impiegato dell'ufficio tirocini di approvare una domanda di tirocinio già 
   * presente nel sistema.
   * 
   * @param idDomanda Long che rappresenta l'identificatore della domanda di tirocinio
   *                  da respingere
   * 
   * @throws IdDomandaTirocinioNonValidoException se non esiste alcuna domanda di
   *         tirocinio nel sistema con identificatore uguale ad idDomanda
   * 
   * @throws DomandaTirocinioGestitaException se la domanda identificata da idDomanda
   *         si trova in uno stato diverso da accettata
   */
  @Transactional
  public void approvaDomandaTirocinio(long idDomanda)
         throws IdDomandaTirocinioNonValidoException,
                StatoDomandaNonIdoneoException {
    // Controlla che la domanda esista
    DomandaTirocinio domanda = domandaRepository.findById(idDomanda);
    if (domanda == null) {
      throw new IdDomandaTirocinioNonValidoException();
    }
    
    if (domanda.getStatus() == DomandaTirocinio.ACCETTATA) {
      domanda.setStatus(DomandaTirocinio.APPROVATA);
    } else {
      throw new StatoDomandaNonIdoneoException();
    }
  }
  
  /**
   * Permette ad un impiegato dell'ufficio tirocinio di respingere una domanda di tirocinio già 
   * presente nel sistema.
   * 
   * @param idDomanda Long che rappresenta l'identificatore della domanda di tirocinio
   *                  da rifiutare
   * 
   * @throws IdDomandaTirocinioNonValidoException se non esiste alcuna domanda di tirocinio
   *         nel sistema con identificatore uguale ad idDomanda
   * 
   * @throws DomandaTirocinioGestitaException se la domanda identificata da idDomanda
   *         si trova in uno stato diverso da quello in attesa
   *         
   * @throws CommentoDomandaTirocinioNonValidoException se il commento da associare alla
   *         domanda è nullo o vuoto
   */
  @Transactional
  public void respingiDomandaTirocinio(long idDomanda, String commento)
         throws IdDomandaTirocinioNonValidoException,
                StatoDomandaNonIdoneoException,
                CommentoDomandaTirocinioNonValidoException {
    // Controlla che la domanda esista
    DomandaTirocinio domanda = domandaRepository.findById(idDomanda);
    if (domanda == null) {
      throw new IdDomandaTirocinioNonValidoException();
    }
    
    // Controlla che la richiesta non sia già stata gestita in precedenza ed impostane lo stato
    if (domanda.getStatus() == DomandaTirocinio.ACCETTATA) {
      domanda.setStatus(DomandaTirocinio.RESPINTA);
    } else {
      throw new StatoDomandaNonIdoneoException();
    }
    domanda.setCommentoImpiegato(validaCommento(commento));
  }
  
  /**
   * Controlla che il commento sul rifiuto o respinta di una richiesta sia specificato.
   * 
   * @param commento Stringa che rappresenta il commento da controllare
   * 
   * @return La stringa che rappresenta il commento da controllare bonificata
   * 
   * @throws CommentoDomandaTirocinioNonValidoException se il commento passato come parametro
   *         è nullo oppure è rappresentato da stringa vuota
   */
  public String validaCommento(String commento) 
        throws CommentoDomandaTirocinioNonValidoException {
    if (commento == null) {
      throw new CommentoDomandaTirocinioNonValidoException();
    } else {
      commento = commento.trim();
      
      if (commento.equals("")) {
        throw new CommentoDomandaTirocinioNonValidoException();
      } else {
        return commento;
      }
    }
  }
  
  
  /**
   * Controlla che la data di inizio tirocinio sia specificata e che rientra nell'intervallo
   * prestabilito.
   * 
   * @param dataInizio LocalDate che rappresenta la data da controllare
   * 
   * @param dataFine LocalDate che rappresenta la data di fine in base alla quale controllare
   *                 la data di inizio tirocinio
   * 
   * @return Oggetto LocalDate che rappresenta la data di inizio da controllare bonificata
   * 
   * @throws DataDiFineTirocinioNonValidaException se la data è nulla o se non rispetta i 
   *         parametri stabiliti
   */
  public LocalDate validaDataDiInizioTirocinio(LocalDate dataInizio, LocalDate dataFine) 
      throws DataDiFineTirocinioNonValidaException {
    if (dataInizio == null) {
      throw new DataDiFineTirocinioNonValidaException();
    } else {
      LocalDate oggi = LocalDate.now();
        
      if (dataInizio.isBefore(oggi) || dataInizio.equals(oggi) || dataInizio.isAfter(dataFine)) {
        throw new DataDiFineTirocinioNonValidaException();
      } else {
        return dataInizio;
      } 
    }
  }
  
  /**
   * Controlla che la data di fine tirocinio sia specificata e che rientra nell'intervallo
   * prestabilito.
   * 
   * @param dataInizio LocalDate che rappresenta la data da controllare
   * 
   * @param dataFine LocalDate che rappresenta la data di fine in base alla quale controllare
   *                 la data di inizio tirocinio
   * 
   * @return Oggetto LocalDate che rappresenta la data di fine da controllare bonificata
   * 
   * @throws DataDiFineTirocinioNonValidaException se la data è nulla o se non rispetta i 
   *         parametri stabiliti
   */
  public LocalDate validaDataDiFineTirocinio(LocalDate dataInizio, LocalDate dataFine) 
      throws DataDiFineTirocinioNonValidaException {
    if (dataFine == null) {
      throw new DataDiFineTirocinioNonValidaException();
    } else {
      LocalDate oggi = LocalDate.now();
        
      if (dataFine.isBefore(oggi) || dataFine.equals(oggi) || dataFine.isBefore(dataInizio)) {
        throw new DataDiFineTirocinioNonValidaException();
      } else {
        return dataFine;
      } 
    }
  }
  
  /**
   * Controlla che il numero di cfu specificato sia rientri nell'intervallo
   * prestabilito.
   * 
   * @param cfu int che rappresenta il numero di cfu da controllare
   * 
   * @return int che rappresenta l'intero da controllare bonificato
   * 
   * @throws NumeroCfuNonValidoException se il numero di cfu non rispetta i 
   *         parametri stabiliti
   */
  public int validaCfu(int cfu) throws NumeroCfuNonValidoException {
    if (cfu < 1 || cfu > 18) {
      throw new NumeroCfuNonValidoException();
    } else {
      return cfu;
    }
  }
  
  /**
   * Controlla che il progetto formativo da associare alla domanda di tirocinio non sia archiviato.
   * 
   * @param progetto {@link ProgettoFormativo} che rappresenta il progetto formativo da controllare
   * 
   * @return Oggetto {@link ProgettoFormativo} che rappresenta il progetto da controllare bonificato
   * 
   * @throws ProgettoFormativoArchiviatoException se il progetto formativo è archiviato
   */
  public ProgettoFormativo verificaStatoProgettoFormativo(ProgettoFormativo progetto) 
      throws ProgettoFormativoArchiviatoException {
    if (progetto.getStatus() == 2) {
      throw new ProgettoFormativoArchiviatoException();
    } else {
      return progetto;
    }
  }
}