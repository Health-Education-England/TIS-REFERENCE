import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {AssessmentType} from "./assessment-type.model";
import {AssessmentTypePopupService} from "./assessment-type-popup.service";
import {AssessmentTypeService} from "./assessment-type.service";

@Component({
	selector: 'jhi-assessment-type-delete-dialog',
	templateUrl: './assessment-type-delete-dialog.component.html'
})
export class AssessmentTypeDeleteDialogComponent {

	assessmentType: AssessmentType;

	constructor(private jhiLanguageService: JhiLanguageService,
				private assessmentTypeService: AssessmentTypeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['assessmentType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.assessmentTypeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'assessmentTypeListModification',
				content: 'Deleted an assessmentType'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-assessment-type-delete-popup',
	template: ''
})
export class AssessmentTypeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private assessmentTypePopupService: AssessmentTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.assessmentTypePopupService
				.open(AssessmentTypeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
