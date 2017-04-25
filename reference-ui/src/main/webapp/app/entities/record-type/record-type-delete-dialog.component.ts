import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {RecordType} from "./record-type.model";
import {RecordTypePopupService} from "./record-type-popup.service";
import {RecordTypeService} from "./record-type.service";

@Component({
	selector: 'jhi-record-type-delete-dialog',
	templateUrl: './record-type-delete-dialog.component.html'
})
export class RecordTypeDeleteDialogComponent {

	recordType: RecordType;

	constructor(private jhiLanguageService: JhiLanguageService,
				private recordTypeService: RecordTypeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['recordType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.recordTypeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'recordTypeListModification',
				content: 'Deleted an recordType'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-record-type-delete-popup',
	template: ''
})
export class RecordTypeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private recordTypePopupService: RecordTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.recordTypePopupService
				.open(RecordTypeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
