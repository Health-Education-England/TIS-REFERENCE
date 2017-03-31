import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {RecordTypeDetailComponent} from "../../../../../../main/webapp/app/entities/record-type/record-type-detail.component";
import {RecordTypeService} from "../../../../../../main/webapp/app/entities/record-type/record-type.service";
import {RecordType} from "../../../../../../main/webapp/app/entities/record-type/record-type.model";

describe('Component Tests', () => {

	describe('RecordType Management Detail Component', () => {
		let comp: RecordTypeDetailComponent;
		let fixture: ComponentFixture<RecordTypeDetailComponent>;
		let service: RecordTypeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [RecordTypeDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					RecordTypeService
				]
			}).overrideComponent(RecordTypeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(RecordTypeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(RecordTypeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new RecordType(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.recordType).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
