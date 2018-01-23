<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<%-- Definizione etichette tooltips --%>
<spring:message var="tooltipProgettoFormativoNome" code="tooltip.progettoFormativo.nome"/>
<spring:message var="tooltipCommonNominativo" code="tooltip.common.nominativo" />
<spring:message var="tooltipDomandaTirocinioCfu" code="tooltip.domandaTirocinio.cfu" />
<spring:message var="tooltipCommonSesso" code="tooltip.common.sesso" />
<spring:message var="tooltipDomandaTirocinioCommentoStudente"
                code="tooltip.domandaTirocinio.commentoStudente" />
<spring:message var="tooltipDomandaTirocinioCommentoAzienda"
                code="tooltip.domandaTirocinio.commentoAzienda" />
<spring:message var="tooltipDomandaTirocinioPeriodoTirocinio"
                code="tooltip.domandaTirocinio.periodoTirocinio" />


<%-- Definizione etichette sesso --%>
<spring:message var="labelSessoMaschile" code="label.sesso.maschile" />
<spring:message var="labelSessoFemminile" code="label.sesso.femminile" />


<%-- Definizione elenco --%>
<c:forEach items="${elencoDomandeTirocinio}" var="current" varStatus="loop">
  <c:set var="idModalRespinta" value="domanda-tirocinio-modal-respinta-${loop.index}" />
  <c:set var="idModalApprovazione" value="domanda-tirocinio-modal-approvazione-${loop.index}" />
  
  <ul id="idCollapsible" class="collapsible">
      <li>
        <div class="collapsible-header">
          <div class="col s8 valign-wrapper">
            <i class="material-icons">business</i>
            <c:out value="${current.studente.nome}"/> <c:out value="${current.studente.cognome}"/> - 
            <c:out value="${current.progettoFormativo.nome}" />
            (<c:out value="${current.progettoFormativo.azienda.nome}" />)
          </div>
          <div class="col s4 right-align">
            <span class="right-align"><tags:localDateTime date="${current.data}"/></span>
          </div>
        </div>
        
        <div class="collapsible-body">
          <div class="row row-group">
          
            <div class="col s12">
              <div class="row valign-wrapper" >
                <div class="col s1">
                  <a class="tooltipped tooltipped-icon" 
                     data-position="right"
                     data-delay="50"
                     data-tooltip="${studenteLabel}">
                    <i class ="small material-icons">face</i>
                  </a>      
                </div>
                <div class="col s11">
                  <c:out value="${current.studente.nome}"/> 
                  <c:out value="${current.studente.cognome}"/>
                  (<c:out value="${current.studente.matricola}"/>)
                </div>
              </div>
            </div>
            
            
            <div class="col s12">
              <div class="row valign-wrapper" >
                <div class="col s1">
                  <a class="tooltipped tooltipped-icon" 
                     data-position="right"
                     data-delay="50"
                     data-tooltip="${progettoFormativoLabel}">
                    <i class ="small material-icons">business_center</i>
                  </a>      
                </div>
                <div class="col s11">
                  <c:out value="${current.progettoFormativo.nome}"/> 
                  (<c:out value="${current.progettoFormativo.azienda.nome}"/>)
                </div>
              </div>
            </div>
          
            
            <div class="col s12">
              <div class="row valign-wrapper" >
                <div class="col s1">
                  <a class="tooltipped tooltipped-icon" 
                     data-position="right"
                     data-delay="50"
                     data-tooltip="${periodoTirocinioLabel}">
                    <i class ="small material-icons">event</i>
                  </a>      
                </div>
                <div class="col s11">
                  <tags:localDate date="${current.inizioTirocinio}"/> - 
                  <tags:localDate date="${current.fineTirocinio}"/>
                </div>
              </div>
            </div>
            
          
            <div class="col s12">
              <div class="row valign-wrapper" >
                <div class="col s1">
                  <a class="tooltipped tooltipped-icon" 
                     data-position="right"
                     data-delay="50"
                     data-tooltip="${cfuLabel}">
                    <i class ="small material-icons">school</i>
                  </a>      
                </div>
                <div class="col s11">
                  <c:out value="${current.cfu}"/>
                </div>
              </div>
            </div>
            
            
            <div class="col s12">
              <div class="row valign-wrapper" >
                <div class="col s1">
                  <a class="tooltipped tooltipped-icon" 
                     data-position="right"
                     data-delay="50"
                     data-tooltip="${commentoStudenteLabel}">
                    <i class ="small material-icons">sms</i>
                  </a>      
                </div>
                <div class="col s11">
                  <c:out value="${current.commentoStudente}"/>
                </div>
              </div>
            </div>
            
            
            <c:if test="${not empty current.commentoAzienda}">
	            <div class="col s12">
	              <div class="row valign-wrapper" >
	                <div class="col s1">
	                  <a class="tooltipped tooltipped-icon" 
	                     data-position="right"
	                     data-delay="50"
	                     data-tooltip="${commentoAziendaLabel}">
	                    <i class ="small material-icons">feedback</i>
	                  </a>      
	                </div>
	                <div class="col s11">
	                  <c:out value="${current.commentoAzienda}"/>
	                </div>
	              </div>
	            </div>
            </c:if>
          
          </div>
        </div>
      </li>
  </ul>
</c:forEach>

<!-- Script per l'inizializzazione e la validazione dei form -->
<script type="text/javascript"
        src="<c:url value="/resources/js/domandeUfficioTirocini.js" />" ></script>