import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {CurriculumSubType} from "./curriculum-sub-type.model";
import {CurriculumSubTypePopupService} from "./curriculum-sub-type-popup.service";
import {CurriculumSubTypeService} from "./curriculum-sub-type.service";

@Component({
	selector: 'jhi-curriculum-sub-type-delete-dialog',
	templateUrl: './curriculum-sub-type-delete-dialog.component.html'
})
export class CurriculumSubTypeDeleteDialogComponent {

	curriculumSubType: CurriculumSubType;

	constructor(private jhiLanguageService: JhiLanguageService,
				private curriculumSubTypeService: CurriculumSubTypeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['curriculumSubType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.curriculumSubTypeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'curriculumSubTypeListModification',
				content: 'Deleted an curriculumSubType'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-curriculum-sub-type-delete-popup',
	template: ''
})
export class CurriculumSubTypeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private curriculumSubTypePopupService: CurriculumSubTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.curriculumSubTypePopupService
				.open(CurriculumSubTypeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
